/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: SelectSqlStatement, 2.4.1999 //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.util.*;

/** builds a select sql string with the help of ClassMap and its AttributeMaps
  */
public class SelectSqlStatement extends SqlStatement
{
    private static final long serialVersionUID = 619163877791339216L;

	public SelectSqlStatement(ClassMap map, PersistentObject pObj)
    {
        super(map,pObj);
    }

    /** builds select sql string for one row in db (one instance of a domain class
      * @param attribute "WHERE attribute = ..." (search criteria)
      * @param value the "WHERE .... = value"
      */
    public void buildForObject(String attribute, Object value)
    {
        Enumeration e;
        int i = 0;

        buf.setLength(0);
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
        buf.append(" WHERE " + map.getAttribute(attribute).getColumn() + " = ");
        if (value.getClass().getName().equals("java.lang.String"))
            buf.append("'"+value+"'");
        else
            buf.append(""+value+"");
    }

    /** builds select sql string for inner table in one row in db (one instance of a domain class)
      * @param nestedAttr "SELECT nestedAttr FROM ..."
      * @param pObj is not needed and therefore null
      * @param searchAttr "WHERE attribute = ..." (search criteria)
      * @param val the "WHERE .... = val2"
      */
    public void buildForInnerTable(String nestedAttr, PersistentObject pObj, String searchAttr, Object val)
    {
        buf.setLength(0);
        buf.append("SELECT * FROM THE (SELECT " + map.getAttribute(nestedAttr).getColumn());
        buf.append(" FROM " + map.getTablename());
        buf.append(" WHERE " + map.getAttribute(searchAttr).getColumn() + " = ");
        if (val.getClass().getName().equals("java.lang.String"))
             buf.append("'"+val+"')");
        else
             buf.append(""+val+")");

    }

}