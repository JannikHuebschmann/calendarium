/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: RetrieveCriteria, 4.2.1999   //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

/** builds select sql statement in order to retrieve a collection of "domain objects"
  * RetrieveCriteria creates corresponding instances of subclasses SelectionCriteria
  * @see SelectionCriteria
  */
public class DeleteCriteria extends PersistentCriteria
{
    public DeleteCriteria(String classname, String kuerzel)
    {
        super(classname,kuerzel);
    }
    /** build select sql statement
      */
    public String buildForObject()
    {
        return new String("DELETE FROM " + map.getTablename() + " WHERE ");
    }

    /** build sql select statement for inner table
      * @param nestedAttr attribute which maps to nested table
      */
    public String buildForInnerTable(String nestedAttr)
    {
        StringBuffer buf = new StringBuffer();

        buf.append("DELETE FROM THE (SELECT ");
        buf.append(map.getAttribute(nestedAttr).getColumn());
        buf.append(" FROM " + map.getTablename() + " WHERE ");
        return new String(buf);
    }


}