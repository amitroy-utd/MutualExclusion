/* 
 *  Node Data Structure
 */

import java.io.Serializable;
import java.util.*;

public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nodeId;
	int port;
	String hostname;
	List<Node> discoveredNodes;
	String neighbours;
	
	
	
	public Node()
	{
		discoveredNodes = null;
	}
	
	public void addNeighbor(Node n)
	{
		if(discoveredNodes == null)
		{
			discoveredNodes = new ArrayList<Node>();
		}
		discoveredNodes.add(n);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if(discoveredNodes != null)
		{
			for(Node n : discoveredNodes)
			{
				sb.append(n.nodeId+", ");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}
	
}
