import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
 
 
public class Server {
 
    ServerSocket myServerSocket;
    boolean ServerOn = true;
 
 
    public Server(final int portnum) 
    { 
        try
        { 
        	System.out.println("In server");
            myServerSocket = new ServerSocket(portnum); 
        } 
        catch(IOException ioe) 
        { 
            System.out.println("Could not create server socket on port . Quitting."); 
            System.exit(-1); 
        } 
 
 
 
 
        //Calendar now = Calendar.getInstance();
        //SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        //System.out.println("It is now : " + formatter.format(now.getTime()));
 
 
 
 
        // Successfully created Server Socket. Now wait for connections. 
        while(ServerOn) 
        {                        
            try
            { 
                // Accept incoming connections. 
                Socket clientSocket = myServerSocket.accept(); 
 
                // accept() will block until a client connects to the server. 
                // If execution reaches this point, then it means that a client 
                // socket has been accepted. 
 
                // For each client, we will start a service thread to 
                // service the client requests. This is to demonstrate a 
                // Multi-Threaded server. Starting a thread also lets our 
                // MultiThreadedSocket Server accept multiple connections simultaneously. 
 
                // Start a Service thread 
 
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
                cliThread.start(); 
 
            } 
            catch(IOException ioe) 
            { 
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :"); 
                ioe.printStackTrace(); 
            } 
 
        }
 
        try
        { 
            myServerSocket.close(); 
            System.out.println("Server Stopped"); 
        } 
        catch(Exception ioe) 
        { 
            System.out.println("Problem stopping server socket"); 
            System.exit(-1); 
        } 
 
 
 
    } 
 
  //  public static void main (String[] args) 
    //{ 
      //  new Server(1000);        
    //} 
 
 
    class ClientServiceThread extends Thread 
    { 
        Socket myClientSocket;
        boolean m_bRunThread = true; 
 
        public ClientServiceThread() 
        { 
            super(); 
        } 
 
        ClientServiceThread(Socket s) 
        { 
            myClientSocket = s; 
 
        } 
 
        public void run() 
        {            
            
            ObjectInputStream in = null;
            //Socket socket;
 
            // Print out details of this connection 
            System.out.println("Accepted Client "); 
 
            try
            {                                
            	in = new ObjectInputStream(myClientSocket.getInputStream());
				Object obj = in.readObject();
				
				MessageStruct msgrcvd = null;
                
                msgrcvd = (MessageStruct) obj;
                    
                if (msgrcvd.msgType == 0) // the received message is a request from a node for keys 
                {
                	Map.Entry<String, String> entry1 = Algorithm.cs_queue.entrySet().iterator().next();
					final String currenttimestmpnode=entry1.getKey();	
					
                	// check if the node is executing in critical section
                	synchronized(Algorithm.cs_flag)
                	{
                		switch(Algorithm.cs_flag)
                    	{
                    		case "enabled":
                    						// put in queue because current node is in critical section
                    						Algorithm.cs_queue.put(msgrcvd.timestamp+"_"+msgrcvd.nodeid,Integer.toString(msgrcvd.nodeid)); //""+NodeID+""
                    						break;
                    		case "wait":
                    						//String key = Algorithm.cs_queue.;
                    						boolean ourPriority = false;
                    						
                    						long timestamp = Long.parseLong(currenttimestmpnode.split("_")[0]);
                    						// check the timestamps and then nodeid
                    						//determine_priority();
                    						if (msgrcvd.timestamp > timestamp)
                    						{
                    							ourPriority = true;
                    						}
                    						else if (msgrcvd.timestamp == timestamp)
                    						{
                    							if (msgrcvd.nodeid < Integer.parseInt(currenttimestmpnode.split("_")[1]) )
                    							{
                    								ourPriority = false;
                    							}
                    							else
                    							{
                    								ourPriority = true;
                    							}
                    						}
                    						else
                    						{
                    							ourPriority = true;
                    						}
                    						if (ourPriority)
                    						{
                    							//do not send the response keys, put in our queue
                    							Algorithm.cs_queue.put(msgrcvd.timestamp+"_"+msgrcvd.nodeid,Integer.toString(msgrcvd.nodeid)); //""+NodeID+""
                        						
                    						}
                    						else // its not our priority
                    						{
                    							try
                    							{
                    								String []nodeNetInfo=Algorithm.map.get(msgrcvd.nodeid).split(":");
                    								
                    								MessageStruct ms = new MessageStruct(2,Integer.parseInt(currenttimestmpnode.split("_")[1]),Long.parseLong(currenttimestmpnode.split("_")[0]),Algorithm.shared_keys.get(msgrcvd.nodeid));
                    								
                    					           	sendResponseMessage(ms, nodeNetInfo[0], Integer.parseInt(nodeNetInfo[1]));
                    					           	
                    							}
                    							catch (Exception e)
                    							{
                    								e.printStackTrace();
                    							}
                    							// remove the keys from shared key
                    							Algorithm.shared_keys.remove(msgrcvd.nodeid);
                    						}
                    			
                    						//String firstOther = Algorithm.cs_queue.get(Algorithm.cs_queue.firstKey());
                    						break;
                    		case "disabled":
                    						// send the keys to the node
			                    			try
			    							{
			    								String []nodeNetInfo=Algorithm.map.get(msgrcvd.nodeid).split(":");
			    								
			    								MessageStruct ms = new MessageStruct(1,Integer.parseInt(currenttimestmpnode.split("_")[1]),Long.parseLong(currenttimestmpnode.split("_")[0]),Algorithm.shared_keys.get(msgrcvd.nodeid));
			    								
			    					           	sendResponseMessage(ms, nodeNetInfo[0], Integer.parseInt(nodeNetInfo[1]));
			    					           	
			    							}
			    							catch (Exception e)
			    							{
			    								e.printStackTrace();
			    							}
			    							// remove the keys from shared key
			    							Algorithm.shared_keys.remove(msgrcvd.nodeid);
                    						
                    						break;
                    		default: System.out.println("Default");
                    	}
                	}
                	
                }
                else if (msgrcvd.msgType == 1) // message received is a response with keys
                {
                	//add the keys to the shared list
                	Algorithm.shared_keys.put(msgrcvd.nodeid, msgrcvd.Keys);
                	//check for n-1 keys and call cs handler of algorithm module
                	//if n-1 keys not there do nothing
                	
                	
                }
                else if (msgrcvd.msgType == 2) // message received is a response with a request again
                {
                	//put the shared keys
                	Algorithm.shared_keys.put(msgrcvd.nodeid, msgrcvd.Keys);
                	// chk for n-1 keys and call cs handler
                	//put the request of the incoming node in the queue
                	Algorithm.cs_queue.put(msgrcvd.timestamp+"_"+msgrcvd.nodeid,Integer.toString(msgrcvd.nodeid)); //""+NodeID+""
                	
                }
                // Run in a loop until m_bRunThread is set to false 
                //while(m_bRunThread) 
                //{                    
                //                       
                //} 
            } 
            catch(Exception e) 
            { 
                e.printStackTrace(); 
            } 
            finally
            { 
                // Clean up 
                try
                {                    
                    in.close(); 
                    
                    myClientSocket.close(); 
                    System.out.println("...Stopped"); 
                } 
                catch(IOException ioe) 
                { 
                    ioe.printStackTrace(); 
                } 
            } 
        } 
        public void sendResponseMessage(Object obj, String hostname, int portno)
    	{
        	System.out.println("Sending response");
        	try
			{
				
				Socket socket=new Socket(hostname,portno);
				
				ObjectOutputStream out = null;
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(obj);
	           	out.flush();
	           	out.close();
	           	socket.close();
	           	
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
    	}
 
    } 
}
