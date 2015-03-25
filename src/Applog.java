import java.io.*;
import java.util.Map;

public class Applog {
	public static void CreateWriteFile(int nodeid, String content)
	{

		File f = null;
		long timestamp=System.currentTimeMillis();
		boolean bool = false;
		try
		{
			// create new file
			f = new File(Integer.toString(nodeid)+".txt");

			// tries to create new file in the system
			if(!f.exists())
			{
				bool = f.createNewFile();
			}

			//System.out.println("File created: "+bool);

			FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("\nNode id:"+nodeid+" "+content+" Timestamp:"+timestamp);
			if(content.equals("start"))
			{
	 			String key_str="  ";
	 			for (Map.Entry<Integer, String> entry : Algorithm.shared_keys.entrySet())
				{
					Integer key = entry.getKey();
					String value = entry.getValue();
					System.out.println("in File application key ===" +key+ "values"+value);
					key_str = key_str.concat(key + " => " + value).concat("   ");
				}
	 			bw.write("keys="+key_str);
			}
 			bw.close();               
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
