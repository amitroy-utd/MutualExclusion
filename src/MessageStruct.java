import java.io.Serializable;
class MessageStruct implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String msgType;
    int nodeid ;
    long timestamp;
    String key;
    
    

    public  MessageStruct( String msgType, int nodeid, long timestamp,String key)
    {
        this.nodeid=nodeid;
       this.msgType=msgType;
       this.timestamp=timestamp;
       this.key=key;
    }

   
}