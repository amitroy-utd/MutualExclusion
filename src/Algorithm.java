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
	public static Map<Long, String> cs_queue=Collections.synchronizedMap(new TreeMap<Long, String>());
	public static Map<Integer, String> shared_keys=Collections.synchronizedMap(new TreeMap<Integer, String>());	
	public static int NodeID=0;
	public static String cs_flag="disabled";
	static Socket socket = null;
	static int cs_handler_call_flag=0;
	static long currentProcessingRequest;
	

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
	public static boolean checkMyRequestExist()
	{
		Collection<String> collectionString = ((TreeMap<Long, String>) cs_queue).values();
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
						Map.Entry<Long, String> entry_1 = cs_queue.entrySet().iterator().next();
						final Long currentProcessingRequest1=entry_1.getKey();
						final String currentProcessingRequest1_nodeid=entry_1.getValue();
						if(Integer.parseInt(currentProcessingRequest1_nodeid)!=NodeID)
						{
							//cs_flag="";
							System.out.println("sending response to "+ Integer.parseInt(currentProcessingRequest1_nodeid));
							final int requesting_node=Integer.parseInt(currentProcessingRequest1_nodeid);
							String []nodeNetInfo=map.get(requesting_node).split(":");
				        	 String keysToSend = shared_keys.get(requesting_node);
							
				        	 try {
						        	//System.out.println("starting client");
					            	MessageStruct ms=new MessageStruct(1,NodeID,0,keysToSend);
									new ClientDemo().startClient(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]),ms);
									shared_keys.remove(requesting_node);
								} catch (Exception e) {
									System.out.println("Something falied: " + e.getMessage());
									e.printStackTrace();
								}
								
				         
								
							cs_queue.remove(currentProcessingRequest1);
							System.out.println("removed from queue");
							System.out.println("In cs_handler_other="+ currentProcessingRequest1);
							
						}
					}
				}
			}
			
			try {
				//System.out.println("going to sleep");
				Thread.sleep(1000);
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
			Map.Entry<Long, String> entry_1 = cs_queue.entrySet().iterator().next();
			currentProcessingRequest=entry_1.getKey();
			final String currentProcessingRequest_nodeid=entry_1.getValue();
			if(Integer.parseInt(currentProcessingRequest_nodeid)==NodeID)
			{
				synchronized(cs_flag){
					if(checkKeys()==true)
					{	
						System.out.println("critical section executing");						
						cs_flag="enabled";	
						/*Thread t = new Thread(new Runnable() {
								public void run()
								{
									Applog.CreateWriteFile(NodeID, "start");
								
								}
							});
						t.start(); */
						    
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
								if(key!=NodeID && shared_keys.containsKey(key)==false)
								{
									System.out.println("sending request to: "+key);
									      	 String []nodeNetInfo=value.split(":");
												
										            try {
											        	//System.out.println("starting client");
										            	MessageStruct ms=new MessageStruct(0,NodeID,getTimeStamp(currentProcessingRequest, currentProcessingRequest_nodeid),"");
														new ClientDemo().startClient(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]),ms);
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
			try {
				//System.out.println("going to sleep");
				Thread.sleep(1000);
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
		cs_queue.put(getQueueKey(timestamp, Integer.toString(NodeID)),Integer.toString(NodeID)); //""+NodeID+""
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
		/*Thread t = new Thread(new Runnable() {
			public void run()
			{
				Applog.CreateWriteFile(NodeID, "end");
			
			}
		});
		t.start(); */
		cs_queue.remove(currentProcessingRequest);
		System.out.println("In cs_leave==="+currentProcessingRequest+"  "+Integer.toString(NodeID));
			 		 
	}
	public static long getTimeStamp(long timestampkey,String nodeid)
	{
		int timestampkey_length = (int) Math.log10(timestampkey) + 1;
		int nodeid_length=nodeid.length();
		int timestamp_length=timestampkey_length-nodeid_length;
		String timestamp=Long.toString(timestampkey).substring(0, timestamp_length);
		return Long.parseLong(timestamp);
	}
	public static long getQueueKey(long timestamp,String nodeid)
	{
		String timestampkey=Long.toString(timestamp)+nodeid;
		return Long.parseLong(timestampkey);
	}
}
