import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class AlgoValidator {

	public static void main(String[] args) throws IOException {
		File fin=new File("RC_log.txt");
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
				if(Long.parseLong(cur_line1[1]) >= Long.parseLong(cur_line[1]) && Long.parseLong(cur_line1[1])<=Long.parseLong(cur_line[2]))
				{
					System.out.println("Violation Occured!");
					return;
				}
				else if(Long.parseLong(cur_line1[2]) >= Long.parseLong(cur_line[1]) && Long.parseLong(cur_line1[2])<=Long.parseLong(cur_line[2]))
				{
					System.out.println("Violation Occured!");
					return;
				}
			}
		}
		System.out.println("No Critical Section violaiton occured!");	

	}

}
