import java.io.*;
import java.util.Map;

public class Applog {
	public static void CreateWriteFile(int nodeid,long timestamp, String content)
	{

		File f = null;
		boolean bool = false;		
		try
		{
			// create new file
			f = new File("RC_log.txt");

			// tries to create new file in the system
			if(!f.exists())
			{
				bool = f.createNewFile();				
			}
			FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			if(content.equals("start")){
				bw.write(nodeid+","+timestamp);
			}
			else{
				bw.write(","+timestamp+"\n");
			}						
			bw.close();               
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
