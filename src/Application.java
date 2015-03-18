import java.io.BufferedReader;
import java.io.FileReader;
public class Application {
	public  int NodeID;
	public  int cs_max_request=0;
	public  int cs_request_delay=0;
	public  int cs_exec_duration=0;
	public void readNodeCSDetails(int nodeId, String topologyFile)
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
				if(nodeId==Integer.parseInt(data[0]));
				{
					cs_max_request=Integer.parseInt(data[2]);
					cs_request_delay=Integer.parseInt(data[3]);
					cs_exec_duration=Integer.parseInt(data[4]);
					break;									
				}				
			}
			br.close();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void call_cs_enter()
	{
		// execute according to read information
	}
	
	public void cs_execute()
	{
		FileReadingWriting.CreateWriteFile(NodeID, "", "aos_cs.txt", cs_exec_duration);
		Algorithm.cs_leave();
	}
	
	public static void main(String []args)
	{
		Application ap=new Application();
		ap.readNodeCSDetails(1, "topology.txt");
		ap.cs_execute();
	}
}
