/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: InsertCriteria, 2.4.1999     //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.util.Enumeration;

/** builds select sql statement in order to retrieve a collection of "domain objects"
  * RetrieveCriteria creates corresponding instances of subclasses SelectionCriteria
  * @see SelectionCriteria
  */
public class InsertCriteria extends PersistentCriteria
{
    private PersistentObject object;

    public InsertCriteria(String classname, String kuerzel, PersistentObject pObj)
    {
        super(classname,kuerzel);
        object = pObj;
    }

    /** builds insert sql string for one row in db (one instance of a domain class
      */
    public String buildForObject()
    {
        Enumeration e;
        int i = 0;
        StringBuffer buf = new StringBuffer(), buf1 = new StringBuffer(),buf2 = new StringBuffer();

        e = map.getAttributes().elements();
        while (e.hasMoreElements())
        {
            AttributeMap am = (AttributeMap)e.nextElement();
            if (!am.isNested())
            {
                i++;
                if (i != 1)
                {
                    buf1.append(" , ");
                    buf2.append(" , ");
                }
                buf1.append(am.getColumn());
                String s = object.getValue(am.getAttribute()).getClass().getName();
                if (s.equals("java.lang.String"))
                    buf2.append("'"+object.getValue(am.getAttribute())+"'");
                else
                    buf2.append(""+object.getValue(am.getAttribute())+"");

            }
        }
        buf.append("INSERT INTO " + map.getTablename() + "( ");
        buf.append(new String(buf1));
        buf.append(") VALUES (");
        buf.append(new String(buf2));
        buf.append(")");
        return new String(buf);

    }


    /** builds insert sql string for inner table in one row in db (one instance of a domain class)
      * @param nestedAttr "SELECT nestedAttr FROM ..."
      */
    public String buildForInnerTable(String nestedAttr)
    {
        return null;
    }



}