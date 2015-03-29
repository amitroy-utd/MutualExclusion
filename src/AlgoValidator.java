import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class AlgoValidator {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br;
		ArrayList<String> arr=new ArrayList<String>();
		br = new BufferedReader(new FileReader("topology.txt"));
		String line = "";
		ArrayList<String> data=new ArrayList<String>();
		while((line=br.readLine())!=null)
		{
			data.add(line.split(" ")[0]);
		
		}
		
		for(String d:data)
		{
			BufferedReader br1 = new BufferedReader(new FileReader(d+".txt"));		 
			String line1 = null;
			while ((line1 = br1.readLine()) != null) {
				arr.add(line1);
			}
			br1.close();
		}
		br.close();
		
		for(int i=0;i<arr.size();i++)
		{
			String []cur_line=arr.get(i).split(",");
			if(i%100==0)
			{
				System.out.print(".");
			}
			for(int j=i+1;j<arr.size();j++)
			{				
				String []cur_line1=arr.get(j).split(",");
				if(Long.parseLong(cur_line1[1]) >= Long.parseLong(cur_line[1]) && Long.parseLong(cur_line1[1])<=Long.parseLong(cur_line[3]))
				{
					System.out.println("Violation Occured!");
					return;
				}
				else if(Long.parseLong(cur_line1[3]) >= Long.parseLong(cur_line[1]) && Long.parseLong(cur_line1[3])<=Long.parseLong(cur_line[3]))
				{
					System.out.println("Violation Occured!");
					return;
				}
			}
		}
		System.out.println("No Critical Section violaiton occured!");	
		
		
		
		
		
		
		
		
		
		
		/*File fin=new File("RC_log.txt");
		FileInputStream fis = new FileInputStream(fin);
		ArrayList<String> arr=new ArrayList<String>();
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			arr.add(line);
		}	 
		br.close();
		for(int i=0;i<arr.size();i++)
		{
			String []cur_line=arr.get(i).split(",");
			if(i%100==0)
			{
				System.out.print(".");
			}
			for(int j=i+1;j<arr.size();j++)
			{				
				String []cur_line1=arr.get(j).split(",");
				if(Long.parseLong(cur_line1[1]) >= Long.parseLong(cur_line[1]) && Long.parseLong(cur_line1[1])<=Long.parseLong(cur_line[3]))
				{
					System.out.println("Violation Occured!");
					return;
				}
				else if(Long.parseLong(cur_line1[3]) >= Long.parseLong(cur_line[1]) && Long.parseLong(cur_line1[3])<=Long.parseLong(cur_line[3]))
				{
					System.out.println("Violation Occured!");
					return;
				}
			}
		}
		System.out.println("No Critical Section violaiton occured!");	
*/
	}

}
