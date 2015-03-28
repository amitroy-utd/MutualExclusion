
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Algorithm {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public static Map<Integer, String> map=Collections.synchronizedMap(new TreeMap<Integer, String>());	
	//public static Map<String, String> cs_queue=Collections.synchronizedMap(new TreeMap<String, String>());
	public static List<String> cs_queue = Collections.synchronizedList(new LinkedList<String>());
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
				//System.out.println("lineis:"+line);
				String data[] = line.split(" ");
				map.put(Integer.parseInt(data[0]),data[1]);	
				//System.out.println(Integer.parseInt(data[0])+"ll"+data[1]);
				//System.out.println("data length"+ data.length);
				if(data.length==6)
				{
					if(nodeId==Integer.parseInt(data[0]))
					{
						//System.out.println("inside data "+ Integer.parseInt(data[0]) +"node id is"+nodeId);
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
			for (Map.Entry<Integer, String> entry : shared_keys.entrySet())
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
			System.out.println("Shared key size is"+shared_keys.size());
			for (Map.Entry<Integer, String> entry : shared_keys.entrySet())
			{
				Integer key = entry.getKey();
				String value = entry.getValue();
				System.out.println( "shared keys all present"+ key + " => " + value);
			}
			return true;
		}
		return false;
	}
	/*public static boolean checkMyRequestExist()
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
	}*/
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
						//String entry_1 = cs_queue.get(0);
						final String currentProcessingRequest1=cs_queue.get(0);	
						System.out.println("######################Other Current Proc is"+currentProcessingRequest1+"our node"+Project2.CurrentNodeId);
						System.out.println("###################### node id:"+currentProcessingRequest1.split("_")[1]);
						if(Integer.parseInt(currentProcessingRequest1.split("_")[1])!=NodeID)
						{
							synchronized(shared_keys)
			        		 {
								//cs_flag="";
								System.out.println("sending response to "+ Integer.parseInt(currentProcessingRequest1.split("_")[1]));
								final int requesting_node=Integer.parseInt(currentProcessingRequest1.split("_")[1]);
								String []nodeNetInfo=map.get(requesting_node).split(":");
								String keysToSend = shared_keys.get(requesting_node);
								System.out.println("****requesting node is:"+requesting_node +"**keystosend are:"+keysToSend);
							
								try {
				        		 
						        	//System.out.println("starting client");
					            	MessageStruct ms=new MessageStruct(1,NodeID,Long.parseLong(currentProcessingRequest1.split("_")[0]),keysToSend);
									new ClientDemo().startClient(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]),ms);
									shared_keys.remove(requesting_node);
								
								} catch (Exception e) {
									System.out.println("Something falied: " + e.getMessage());
									e.printStackTrace();
								}
								
				         
								
								cs_queue.remove(currentProcessingRequest1);
								System.out.println("removed from queue"+currentProcessingRequest1+"our node"+Project2.CurrentNodeId);
								System.out.println("In cs_handler_other="+ currentProcessingRequest1);
							
			        		 }	
						}
					}
				}
			}
			
			try {
				//System.out.println("going to sleep");
				Thread.sleep(3);
				//System.out.println("woke up");
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
			//String entry_1 = cs_queue.get();
			currentProcessingRequest=cs_queue.get(0);
			System.out.println("######################Own Current Proc is"+currentProcessingRequest);
			System.out.println("###################### Node is:"+currentProcessingRequest.split("_")[1]);
			if(Integer.parseInt(currentProcessingRequest.split("_")[1])==NodeID)
			{
				synchronized(cs_flag){
					if(checkKeys()==true)
					{	
						System.out.println("critical section executing");						
						cs_flag="enabled";	
						final long timestamp=System.currentTimeMillis();
						Thread t = new Thread(new Runnable() {
								public void run()
								{
									Applog.CreateWriteFile(NodeID,timestamp,"start");
								
								}
							});
						t.start(); 
						    
						return;							
					}		
					else
					{
						if(cs_flag.equals("disabled"))
						{
							
							System.out.println("in wait");
							cs_flag="wait";
							for (Map.Entry<Integer, String> entry : map.entrySet())
							{
								Integer key = entry.getKey();
								final String value = entry.getValue();					
								synchronized(shared_keys)
				        		{
								if(key!=NodeID && shared_keys.containsKey(key)==false)
								{
									System.out.println("sending request to: "+key +"from node"+Project2.CurrentNodeId+"for tmstmp:"+currentProcessingRequest);
									      	 String []nodeNetInfo=value.split(":");
												
										            try {
											        	//System.out.println("starting client");
										            	MessageStruct ms=new MessageStruct(0,NodeID,Long.parseLong(currentProcessingRequest.split("_")[0]),"");
										            	//added
										            	ObjectOutputStream out = null;
								            			socket = new Socket(nodeNetInfo[0], Integer.parseInt(nodeNetInfo[1]));
								             
								            			out = new ObjectOutputStream(socket.getOutputStream());
								            			out.writeObject(ms);
								            			//System.out.println("wrote to server"+obj+"ipadd and port"+IPAddress+port);
								            			
								            			out.flush();
								            			out.close();
										            	//added
								            			//new ClientDemo().startClient(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]),ms);
													} catch (Exception e) {
														System.out.println("Something falied: " + e.getMessage());
														e.printStackTrace();
													}
									       											
								}
				        		}
							}
						}
						
					}
				}
			}
			try {
				//System.out.println("going to sleep");
				Thread.sleep(3);
				//System.out.println("waking up");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}				
	
	public static void cs_enter()
	{
		long timestamp=System.currentTimeMillis();		
		cs_queue.add(timestamp+"_"+NodeID); //""+NodeID+""
		System.out.println("In cs_enter==="+timestamp+"_"+NodeID+"  "+Integer.toString(NodeID));
		cs_handler_own();					
		for (Map.Entry<Integer, String> entry : shared_keys.entrySet())
		{
			Integer key = entry.getKey();
			String value = entry.getValue();
			System.out.println( "shared keys when flag is enabled:"+ key + " => " + value);
		}
		return;
	}
	public static void cs_leave()
	{
		cs_flag="disabled";	
		final long timestamp=System.currentTimeMillis();
		Thread t = new Thread(new Runnable() {
			public void run()
			{
				Applog.CreateWriteFile(NodeID,timestamp,"end");
			
			}
		});
		t.start();
		cs_queue.remove(currentProcessingRequest);
		System.out.println("In cs_leave==="+currentProcessingRequest+"  "+Integer.toString(NodeID));
			 		 
	}
}
