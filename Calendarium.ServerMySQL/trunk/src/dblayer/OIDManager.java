//////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: OIDManager, 2.4.1999      //
//////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.sql.*;

/** OIDManager creates an unique key with the help of the database
  */
public class OIDManager implements java.io.Serializable
{
    private static final long serialVersionUID = -474441638306608923L;
	private String kuerzel;

    public void setKuerzel(String k)
    {
        kuerzel = k;
    }

    /** generate next unique key
      */
    public long getKey()
    {
    	long id;
    	
    	//Read actual ID out of the table keygenerator
    	ConnectionManager.lock(kuerzel, "keygenerator");
    	ResultSet res = PersistenceBroker.processSql(kuerzel,"SELECT id FROM keygenerator");
    	
    	try
        {
    		id=0;
    		//System.out.println("RES aus OID: "+res);
    		if(res.next())
    		{
    			id = res.getLong("ID");
    		}
    		    		
    		//res.close();
      
	    	//Inkrement this ID by 1 and insert that new ID into the table
	    	id++;
	    	ConnectionManager.lock(kuerzel, "keygenerator");
	    	PersistenceBroker.processSql(kuerzel,"UPDATE keygenerator SET id="+id+" WHERE id="+(id-1));
	    	
	    	
	    	return id;
    	}catch (SQLException s) {System.out.println(s);}
        
        return 0;
    }

    /** return current key from the table keygenerator
      */
    public long readKey()
    {
    	ConnectionManager.lock(kuerzel, "keygenerator");
    	ResultSet res = PersistenceBroker.processSql(kuerzel,"SELECT id FROM keygenerator");
        try
        {
            return res.getLong("ID");
        }catch (SQLException s) {System.out.println(s);}
        return 0;
    }

}


