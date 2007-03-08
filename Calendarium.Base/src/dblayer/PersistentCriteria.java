/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: PersistentCriteria, 2.4.1999 //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.sql.*;

/** this class hierarchy encapsulates the behavior needed to retrieve,
  * update or delete collections of object based on defined criteria
  * in this version only retrieve is supported
  */
public abstract class PersistentCriteria
{
    protected ClassMap map;
    private String kuerzel;

    public PersistentCriteria(String classname, String k)
    {
        map = ClassMappings.getClass(classname);
        kuerzel = k;
    }

    public abstract String buildForObject();
    public abstract String buildForInnerTable(String nestedAttr);

    /** construct sql clause
      * @param attributeName  "WHERE attributeName = ..."
      * @param value  "WHERE ... = value"
      */
    public String addSelectEqualTo(String attributeName, Object value)
    {
        SelectionCriteria sc = new EqualToCriteria(map, attributeName, value);
        return new String(sc.asSqlClause());
    }

    /** construct sql clause
      * @param attributeName  "WHERE attributeName = ..."
      * @param value  "WHERE ... != value"
      */
    public String addSelectNotEqualTo(String attributeName, Object value)
    {
        SelectionCriteria sc = new NotEqualToCriteria(map, attributeName, value);
        return new String(sc.asSqlClause());
    }


    /** construct sql clause
      * @param attributeName  "WHERE attributeName > ..."
      * @param value  "WHERE ... > value"
      */
    public String addSelectGreaterThanEqual(String attributeName, Object value)
    {
        SelectionCriteria sc = new GreaterThanEqualCriteria(map, attributeName, value);
        return new String(sc.asSqlClause());
    }

    /** construct sql clause
      * @param attributeName  "WHERE attributeName < ..."
      * @param value  "WHERE ... < value"
      */
    public String addSelectLessThanEqual(String attributeName, Object value)
    {
        SelectionCriteria sc = new LessThanEqualCriteria(map, attributeName, value);
        return new String(sc.asSqlClause());
    }


    public String addOrCriteria()
    {
        return new String(" OR ");
    }

    public String addWhere()
    {
        return new String(" WHERE ");
    }

    public String addAndCriteria()
    {
        return new String(" AND ");
    }

    public String addCloseBrace()
    {
        return new String(")");
    }

    public String addOpenBrace()
    {
        return new String("(");
    }

    /** executes constructed sql string via PersistenceBroker
      */
    public ResultSet perform(String sql)
    {
        if (sql.indexOf("THE") >= 0) sql = new String(sql+addCloseBrace());  // if inner table is selected ")" must end SFW
        return PersistenceBroker.processSql(kuerzel,sql);
    }

}