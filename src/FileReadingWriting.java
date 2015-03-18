import java.io.*;

public class FileReadingWriting {
	
   public static void CreateWriteFile(int nodeid, String content, String filename,int cs_exec_duration)
   {
	   
	      File f = null;
	      boolean bool = false;
	      long start_timestamp=System.currentTimeMillis();
	      
	      content="\n"+content;
	     try
	      {
	         // create new file
	         f = new File(filename);
	         
	         // tries to create new file in the system
	         if(!f.exists())
	         {
	        	 bool = f.createNewFile();
	         }
	       
	         //System.out.println("File created: "+bool);
	                  
	        	FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
	 			BufferedWriter bw = new BufferedWriter(fw);
	 			bw.write("\nNode id:"+nodeid+" Start Timestamp:"+start_timestamp);
	 			Thread.sleep(cs_exec_duration*1000);
	 			long stop_timestamp=System.currentTimeMillis();
	 			bw.write("\nNode id:"+nodeid+" End Timestamp:"+stop_timestamp);
	 			bw.close();         
	      }
	      catch(Exception e)
	      {
	         e.printStackTrace();
	      }
   }
}