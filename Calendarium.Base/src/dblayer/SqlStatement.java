/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: SqlStatement, 2.4.1999       //
////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

/** SqlStatement is the abstract superclass of Insert-, Update-, Delete- and SelectSqlStatement,
  * that build SQL statements based on information encapsulated by ClassMap objects
  * @see ClassMap
  */
public abstract class SqlStatement implements java.io.Serializable
{
    protected PersistentObject object;
    protected ClassMap map;
    protected StringBuffer buf;

    public SqlStatement(ClassMap cm, PersistentObject pObj)
    {
        object = pObj;
        map = cm; //SS
        buf = new StringBuffer();
    }

    public abstract void buildForObject(String attribute, Object value);
    public void buildForInnerTable(String nestedAttr, PersistentObject pObj, String searchAttr, Object val){}

    /** returns SQL statement as String
      */
    public String asString()
    {
        return new String(buf);
    }
}