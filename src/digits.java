import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class digits {
	public static long getTimeStamp(long timestampkey,String nodeid)
	{
		//Long newnumlong = Long.parseLong(newnum);
		int timestampkey_length = (int) Math.log10(timestampkey) + 1;
		int nodeid_length=nodeid.length();
		int timestamp_length=timestampkey_length-nodeid_length;
		String timestamp=Long.toString(timestampkey).substring(0, timestamp_length);
		return Long.parseLong(timestamp);
	}
	public static long getQueueKey(long timestamp,String nodeid)
	{
		String timestampkey=Long.toString(timestamp)+nodeid;
		return Long.parseLong(timestampkey);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long timestamp = System.currentTimeMillis();
		System.out.println("queue key is"+getQueueKey(timestamp,"10"));
		System.out.println("queue key is"+getQueueKey(timestamp,"7"));
		System.out.println("TimestAMP IS"+getTimeStamp(getQueueKey(timestamp,"7"),"7"));

	}

}
