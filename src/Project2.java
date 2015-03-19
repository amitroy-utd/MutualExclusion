import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class Project2 {
	
	public static TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
	public static int CurrentNodeId;
	static int server_port=0;
	static String topology="";
	public static void main(String[] args) throws Exception {
		
		// get details from file
		topology=args[1];
		CurrentNodeId = Integer.parseInt(args[0]);
		Algorithm al=new Algorithm();
		al.getNodeInfoFromFile(CurrentNodeId,topology );	
		server_port=Integer.parseInt(Algorithm.map.get(CurrentNodeId).split(":")[1]);
		//start the server in a separate thread
		
		Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                new Server(server_port);
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
        
        // a delay if needed
        
		// check if all the servers are up and then call application module
        check_all_servers();
        
        Runnable cshandlerothertask = new Runnable() {
            @Override
            public void run() {
                Algorithm.cs_handler_other();
            }
        };
        Thread handlerThread = new Thread(cshandlerothertask);
        handlerThread.start();
        
        // once it comes to this line it means all servers are up
        Application app = new Application();
		app.call_cs_enter();
        
        
		
	}// end of main
	
	public static void copyList()
	
	
	{
		for (Map.Entry<Integer, String> entry : Algorithm.map.entrySet())
		{
			treemap.put(entry.getKey(), entry.getValue());
		}
	}
	
	public static void check_all_servers()
	{
		copyList();
		while(true)
		{
			if (treemap.isEmpty())
			{
				//call the logic because all the servers are up and running 
				System.out.println("all servers up continuing....");
				//break out of the loop
				break;
			}
			
			for (Map.Entry<Integer, String> entry : Algorithm.map.entrySet())
		    {
				boolean remove_entry=false;
				if (treemap.containsKey(entry.getKey()))
				{
					//establish a socket
					String []nodeNetInfo=entry.getValue().split(":");
					try
					{
						Socket socket=new Socket(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]));
						MessageStruct ms = new MessageStruct(9,1,1,"");
						ObjectOutputStream out = null;
						out = new ObjectOutputStream(socket.getOutputStream());
						out.writeObject(ms);
			           	out.flush();
			           	out.close();
						if (socket.isConnected())
						{
							remove_entry = true;
							socket.close();
						}
					}
					catch (Exception e)
					{
						//ignore exception
						
					}
				}
				if (remove_entry)
				{
					System.out.println(entry.getKey() + "is connected");
					treemap.remove(entry.getKey());
				}
		    }
			
		}
	}
	
} //end of class

	
		
	