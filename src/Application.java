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
				
				String data[] = line.split("\\|");
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
	
	public void call_cs_enter() throws InterruptedException
	{
		// execute according to read information
		readNodeCSDetails(Project2.CurrentNodeId, Project2.topology);
		for (int i=0;i<cs_max_request;i++)
		{
			long start_timestamp=System.currentTimeMillis();
			Algorithm.cs_enter();
			//check if flag is enabled 
			cs_execute();
			long end_timestamp=System.currentTimeMillis();
			long diff = end_timestamp - start_timestamp;
			
			long diffSeconds = diff / 1000 % 60;
			// if the difference is less than cs_request_delay then sleep for the diff
			if (diffSeconds < cs_request_delay)
			{
				//sleep before making the second round
				Thread.sleep((cs_request_delay - diffSeconds)*1000);
				
			}
			else
			{
				//make the second cs request
			}
		}
		
	}
	
	public void cs_execute()
	{
		FileReadingWriting.CreateWriteFile(NodeID, "", "aos_cs.txt", cs_exec_duration);
		Algorithm.cs_leave();
	}
	
	//public static void main(String []args)
	//{
		//Application ap=new Application();
		//ap.readNodeCSDetails(1, "topology.txt");
		//ap.cs_execute();
	//}
}
