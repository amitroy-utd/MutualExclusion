import java.io.*;
import java.util.*;
public class Algorithm {
	public static Map<Integer, String> map=Collections.synchronizedMap(new TreeMap<Integer, String>());	
	public static Map<String, String> cs_queue=Collections.synchronizedMap(new TreeMap<String, String>());	
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
		System.out.println("=========>"+getInfoFromMap(2));
	}
	public static void cs_enter()
	{
		 long timestamp=System.currentTimeMillis();
		 cs_queue.put(timestamp+"-"+NodeID,Integer.toString(NodeID)); //""+NodeID+""
		 		 
	}
}