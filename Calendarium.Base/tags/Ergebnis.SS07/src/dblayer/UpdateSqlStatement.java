/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: UpdateSqlStatement 2.4.1999  //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.util.Enumeration;

/** builds an update sql string with the help of ClassMap and its AttributeMaps
  */
public class UpdateSqlStatement extends SqlStatement
{
    private static final long serialVersionUID = -6846902964975602893L;

	public UpdateSqlStatement(ClassMap map, PersistentObject pObj)
    {
        super(map,pObj);
    }

    /** builds update sql string for one row in db (one instance of a domain class
      * @param attribute "WHERE attribute = ..." (search criteria)
      * @param value the "WHERE .... = value"
      */
    public void buildForObject(String attribute, Object value)
    {
        Enumeration e;
        int i = 0;

        buf.setLength(0);
        buf.append("UPDATE " + map.getTablename() + " SET ");
        e = map.getAttributes().elements();
        while (e.hasMoreElements())
        {
            AttributeMap am = (AttributeMap)e.nextElement();
            if (!am.isNested())
            {
                i++;
                if (i != 1)
                    buf.append(" , ");
                buf.append(am.getColumn() + " = ");
                String s = object.getValue(am.getAttribute()).getClass().getName();
                if (s.equals("java.lang.String"))
                    buf.append("'"+object.getValue(am.getAttribute())+"'");
                else
                    buf.append(""+object.getValue(am.getAttribute())+"");
            }
        }
        buf.append(" WHERE " + map.getAttribute(attribute).getColumn() + " = ");
        if (value.getClass().getName().equals("java.langString"))
            buf.append("'"+value+"'");
        else
            buf.append(""+value+"");

    }


}
