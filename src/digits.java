public class digits {
	
	public static void main(String[] args) {
		for(int i=1;i<=20000;i++)
		{
			long timestamp=System.currentTimeMillis();
			if(i%2==0)
			{
				Applog.CreateWriteFile(i,timestamp,"end");
			}
			else{
				Applog.CreateWriteFile(i,timestamp,"start");
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done!");
		//Applog.CreateWriteFile(nodeid,timestamp,content);
		
	}
		
}
