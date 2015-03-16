/*
 * Initializes each node 
 */

import java.io.*;
import java.util.*;
public class InitializeNode {
Node nodeInfo = null;
	
	public void addNodes(String nodeId, String topologyFile)
	{
		BufferedReader br,br1;
		try
		{
			br = new BufferedReader(new FileReader(topologyFile));
			String line = "";
			//String neighbours_arr[] = null;
			ArrayList<String> neighbours_arr = new ArrayList<String>();
			while((line=br.readLine())!=null)
			{
				String data[] = line.split(",");
				if(data[0].equals(nodeId))
				{
					nodeInfo = createNode(data); 
					nodeInfo.addNeighbor(nodeInfo);
					break;
				}
							
			}
			br.close();
			br1 = new BufferedReader(new FileReader(topologyFile));
			if(!nodeInfo.neighbours.equals(""))
			{
				neighbours_arr.addAll(Arrays.asList(nodeInfo.neighbours.split(":")));
			}
			
			//System.out.println("neighbours_arr"+ neighbours_arr[0].trim());
			while((line=br1.readLine())!=null)
			{
				String data[] = line.split(",");				
				if(neighbours_arr.size()==0)
				{
					break;
				}
				else
				{
					//System.out.println("Outside"+ data[0]);
					if(neighbours_arr.contains(data[0]))
					{
						
						Node neighbour=createNode(data);
						nodeInfo.addNeighbor(neighbour);
						//System.out.println("inside"+ data[0]);
					}
				}	
			}			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		} finally {
			if(nodeInfo!=null) {
				//System.out.println(nodeInfo.toString());
				//FileReadingWriting.CreateWriteFile(nodeInfo.nodeId,"Neighbors of Node "+nodeInfo.nodeId+"->" +nodeInfo.toString());
			}
		}

	}
	
	
	
	public  Node createNode(String data[])
	{
		Node n = new Node();
		n.nodeId=data[0];		
		n.hostname = data[1].split("/")[0].trim();
		n.port = Integer.parseInt(data[1].split("/")[1].trim());
		if(data.length>2){
		n.neighbours=data[2];
		}
		else
		{
			n.neighbours="";
		}
		return n;

	}

}


