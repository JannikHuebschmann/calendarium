/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: RetrieveCriteria, 2.4.1999   //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.util.*;

/** builds select sql statement in order to retrieve a collection of "domain objects"
  * RetrieveCriteria creates corresponding instances of subclasses SelectionCriteria
  * @see SelectionCriteria
  */
public class RetrieveCriteria extends PersistentCriteria
{
    public RetrieveCriteria(String classname, String kuerzel)
    {
        super(classname,kuerzel);
        /* Tobi:
         * map = ClassMappings.getClass(classname);
         * kuerzel = kuerzel;
         */
    }
    /** build select sql statement
      */
    public String buildForObject()
    {
        Enumeration e;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        buf.append("SELECT ");
        e = map.getAttributes().elements();
        while (e.hasMoreElements())
        {
            AttributeMap am = (AttributeMap)e.nextElement();
            if (!am.isNested())
            {
                i++;
                if (i != 1) buf.append(" , ");
                buf.append(am.getColumn());
            }
        }
        buf.append(" FROM " + map.getTablename());
        return new String(buf);
    }

    /** build sql select statement for inner table
      * @param nestedAttr attribute which maps to nested table
      */
    public String buildForInnerTable(String nestedAttr)
    {
        LogTool log1 = new LogTool("INFO", "RetrieveCriteria");
    	log1.engageLogging();
    	
    	StringBuffer buf = new StringBuffer();

        buf.append("SELECT * FROM THE (SELECT ");
        buf.append(map.getAttribute(nestedAttr).getColumn());
        buf.append(" FROM " + map.getTablename() + " WHERE ");
        log1.logStatement("Inner: "+new String(buf));
        return new String(buf);
    }


}