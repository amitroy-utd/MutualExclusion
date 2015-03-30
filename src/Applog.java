import java.io.*;
import java.util.Map;

public class Applog {
	public static void CreateWriteFile(int nodeid)
	{

		File f = null;
		boolean bool = false;		
		try
		{
			// create new file
			f = new File(nodeid+".txt");

			// tries to create new file in the system
			if(!f.exists())
			{
				bool = f.createNewFile();				
			}
			FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String log:Algorithm.logData)
			{
				bw.write(log+"\n");
			}
			bw.close();               
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
