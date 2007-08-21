/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: DeleteSqlStatement, 4.2.1999 //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;


/** builds a delete sql string with the help of ClassMap and its AttributeMaps
  */
public class DeleteSqlStatement extends SqlStatement
{
    private static final long serialVersionUID = -4946709006633334625L;

	public DeleteSqlStatement(ClassMap map, PersistentObject pObj)
    {
        super(map,pObj);
    }

    /** builds delete sql string for one row in db (one instance of a domain class
      * @param attribute "WHERE attribute = ..." (search criteria)
      * @param value the "WHERE .... = value"
      */
    public void buildForObject(String attribute, Object value)
    {
        buf.setLength(0);
        buf.append("DELETE FROM " + map.getTablename() + " WHERE ");
        buf.append(map.getAttribute(attribute).getColumn() + " = ");
        if (value.getClass().getName().equals("java.lang.String"))
            buf.append("'"+value+"'");
        else
            buf.append(""+value+"");
    }

    /** builds delete sql string for inner table in one row in db (one instance of a domain class)
      * @param nestedAttr "SELECT nestedAttr FROM ..."
      * @param pObj "..)i...WHERE i.id = val1"
      * @param searchAttr "WHERE attribute = ..." (search criteria)
      * @param val the "WHERE .... = val2"
      */
    public void buildForInnerTable(String nestedAttr, PersistentObject pObj, String searchAttr, Object val)
    {
        buf.setLength(0);
        buf.append("DELETE FROM THE (SELECT " + map.getAttribute(nestedAttr).getColumn());
        buf.append(" FROM " + map.getTablename());
        buf.append(" WHERE " + map.getAttribute(searchAttr).getColumn() + " = ");
        if (val.getClass().getName().equals("java.lang.String"))
            buf.append("'"+val+"'");
        else
            buf.append(""+val+"");
        buf.append(")i WHERE i.id = ");
        buf.append(""+pObj.getID()+"");
     }

}