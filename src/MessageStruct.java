import java.io.Serializable;
class MessageStruct implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int msgType;
    int nodeid ;
    long timestamp;
    String key;
    
    

    public  MessageStruct( int msgType, int nodeid, long timestamp,String key)
    {
        this.nodeid=nodeid;
       this.msgType=msgType;
       this.timestamp=timestamp;
       this.key=key;
    }

   
}