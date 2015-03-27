import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;



public class digits {
	
	public static void main(String[] args) {
		List<String> cs_queue = Collections.synchronizedList(new LinkedList<String>());
		long timestamp=System.currentTimeMillis();
		cs_queue.add(Long.toString(timestamp)+"_"+1);
		timestamp=System.currentTimeMillis();
		cs_queue.add(Long.toString(timestamp)+"_"+3);
		timestamp=System.currentTimeMillis();
		cs_queue.add(Long.toString(timestamp)+"_"+10);
		timestamp=System.currentTimeMillis();
		cs_queue.add(Long.toString(timestamp)+"_"+2);
		ListIterator<String> listIterator = cs_queue.listIterator();
		while (listIterator.hasNext()) 
		{ 
			System.out.println(listIterator.next());
		}
		cs_queue.remove(Long.toString(timestamp)+"_"+1);
		ListIterator<String> listIterator1 = cs_queue.listIterator();
		while (listIterator1.hasNext()) 
		{ 
			System.out.println(listIterator1.next());
		}
		System.out.println("============="+cs_queue.get(0));
		timestamp=System.currentTimeMillis();
		cs_queue.add(Long.toString(timestamp)+"_"+6);
		System.out.println("============="+cs_queue.get(cs_queue.size()-1));
		
		
		
		}
		

	}


