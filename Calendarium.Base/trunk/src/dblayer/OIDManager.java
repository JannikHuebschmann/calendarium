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

    /** return next unique key from sequence
      */
    public long getKey()
    {
        ResultSet res = PersistenceBroker.processSql(kuerzel,"SELECT Sequenz.NEXTVAL FROM KEYGENERATOR");

        try
        {
            if (res.next()) return res.getLong(1);
        }catch (SQLException s) {System.out.println(s);}
        return 0;
    }

    /** return current key from sequence
      */
    public long readKey()
    {
        ResultSet res = PersistenceBroker.processSql(kuerzel,"SELECT Sequenz.CURRVAL FROM KEYGENERATOR");
        try
        {
            if (res.next()) return res.getLong(1);
        }catch (SQLException s) {System.out.println(s);}
        return 0;
    }

}


