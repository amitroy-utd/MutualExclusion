import java.io.*;
import java.util.*;
public class Algorithm {
	public static Map<Integer, String> map=Collections.synchronizedMap(new TreeMap<Integer, String>());	
	public static Map<String, String> cs_queue=Collections.synchronizedMap(new TreeMap<String, String>());
	public static Map<Integer, String> shared_keys=Collections.synchronizedMap(new TreeMap<Integer, String>());
	
	public static int NodeID=0;
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
	public static void main(String []args)
	{
		Algorithm al=new Algorithm();
		al.getNodeInfoFromFile(1, "topology.txt");		
	}
	public static boolean checkKeys()
	{
		if(shared_keys.size()==(map.size()-1))
		{
			return true;
		}
		return false;
	}
	public static void cs_handler()
	{
		if(checkKeys()==true)
		{
			// execute critical section
		}
		else
		{
			// request for keys
		}
	}
	public static void cs_enter()
	{
		long timestamp=System.currentTimeMillis();		
		cs_queue.put(timestamp+"_"+NodeID,Integer.toString(NodeID)); //""+NodeID+""
		cs_handler();
		 		 
	}
}