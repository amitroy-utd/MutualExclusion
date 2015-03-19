import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
public class Algorithm implements Runnable{
	public static Map<Integer, String> map=Collections.synchronizedMap(new TreeMap<Integer, String>());	
	public static Map<String, String> cs_queue=Collections.synchronizedMap(new TreeMap<String, String>());
	public static Map<Integer, String> shared_keys=Collections.synchronizedMap(new TreeMap<Integer, String>());	
	public static int NodeID=0;
	public static String cs_flag="disabled";
	static Socket socket = null;
	static int cs_handler_call_flag=0;
	static String currentProcessingRequest="";
	

	public void getNodeInfoFromFile(int nodeId, String topologyFile)
	{
		BufferedReader br;
		NodeID=nodeId;
		try
		{
			br = new BufferedReader(new FileReader(topologyFile));
			String line = "";
			while((line=br.readLine())!=null)
			{
				String data[] = line.split(" ");
				map.put(Integer.parseInt(data[0]),data[1]);	
				if(data.length==6)
				{
					if(nodeId==Integer.parseInt(data[0]));
					{
						String []key_arr=data[5].split(":");
						for(int i=0;i<key_arr.length;i++)
						{
							String []keys=key_arr[i].split(",");
							if(Integer.parseInt(keys[0])!=nodeId && Integer.parseInt(keys[1])==nodeId)
							{
								shared_keys.put(Integer.parseInt(keys[0]), key_arr[i]);
							}
							else if(Integer.parseInt(keys[1])!=nodeId && Integer.parseInt(keys[0])==nodeId)
							{
								shared_keys.put(Integer.parseInt(keys[1]), key_arr[i]);
							}							
						}						
					}					
				}				
			}
			br.close();
			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				Integer key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + " => " + value);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static String getInfoFromMap(int nodeID)
	{
		return map.get(nodeID);
	}
	
	public static boolean checkKeys()
	{
		if(shared_keys.size()==(map.size()-1))
		{
			return true;
		}
		return false;
	}
	public static boolean checkMyRequestExist()
	{
		Collection<String> collectionString = ((TreeMap<String, String>) cs_queue).values();
		for (Object o : collectionString)
		{
			if(Integer.parseInt(o.toString())==NodeID)
			{
				return true;
			}
			break;
		}
		return false;
	}
	public static void cs_handler_other() 
	{
		while(true)
		{
			if(cs_queue.size()!=0)
			{
				synchronized(cs_flag)
				{
					if(cs_flag.equals("disabled"))
					{
						Map.Entry<String, String> entry_1 = cs_queue.entrySet().iterator().next();
						final String currentProcessingRequest1=entry_1.getKey();	
						if(Integer.parseInt(currentProcessingRequest1.split("_")[1])!=NodeID)
						{
							//cs_flag="";
							final int requesting_node=Integer.parseInt(currentProcessingRequest1.split("_")[1]);
							Thread t = new Thread(new Runnable() {
						         public void run()
						         {
						        	 String []nodeNetInfo=map.get(requesting_node).split(":");
										try {
											socket=new Socket(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]));
											ObjectOutputStream out = null;
											out = new ObjectOutputStream(socket.getOutputStream());
											MessageStruct ms = null;
											ms=new MessageStruct(1,NodeID,Long.parseLong(currentProcessingRequest1.split("_")[0]),shared_keys.get(requesting_node));
											out.writeObject(ms);
								           	out.flush();
								           	out.close();
								           	socket.close();
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (UnknownHostException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}									
						         }
							});
							t.start();	
							cs_queue.remove(currentProcessingRequest1);
							System.out.println("In cs_handler_other="+ currentProcessingRequest1);
							
						}
					}
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void cs_handler_own() 
	{
		while(true)
		{
			Map.Entry<String, String> entry_1 = cs_queue.entrySet().iterator().next();
			currentProcessingRequest=entry_1.getKey();
			if(Integer.parseInt(currentProcessingRequest.split("_")[1])==NodeID)
			{
				synchronized(cs_flag){
					if(checkKeys()==true)
					{					
						cs_flag="enabled";									
						return;							
					}		
					else
					{
						if(cs_flag.equals("disabled"))
						{
							cs_flag="wait";
							for (Map.Entry<Integer, String> entry : map.entrySet())
							{
								Integer key = entry.getKey();
								final String value = entry.getValue();					
								if(key!=NodeID && shared_keys.containsKey(key)==false)
								{
									Thread t = new Thread(new Runnable() {
								         public void run()
								         {
								        	 String []nodeNetInfo=value.split(":");
												try {
													socket=new Socket(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]));
													ObjectOutputStream out = null;
													out = new ObjectOutputStream(socket.getOutputStream());
													MessageStruct ms=new MessageStruct(0,NodeID,Long.parseLong(currentProcessingRequest.split("_")[0]),"");
										           	out.writeObject(ms);
										           	out.flush();
										           	out.close();
										           	socket.close();
												} catch (NumberFormatException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (UnknownHostException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}									
								         }
									});
									t.start();						
								}
							}
						}
						
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}				
	
	public static void cs_enter()
	{
		long timestamp=System.currentTimeMillis();		
		cs_queue.put(timestamp+"_"+NodeID,Integer.toString(NodeID)); //""+NodeID+""
		System.out.println("In cs_enter==="+timestamp+"_"+NodeID+"  "+Integer.toString(NodeID));
		cs_handler_own();						
	}
	public static void cs_leave()
	{
		cs_flag="disabled";	
		cs_queue.remove(currentProcessingRequest);
		System.out.println("In cs_leave==="+currentProcessingRequest+"  "+Integer.toString(NodeID));
			 		 
	}
	public void run(){
		
	}
	
}