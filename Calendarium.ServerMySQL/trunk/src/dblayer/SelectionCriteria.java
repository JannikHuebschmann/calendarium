/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: SelectionCriteria, 2.4.1999  //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

/** this class hierarchy encapsulates the behavior needed to compare
  * a single attribute to a given value. There is one subclass for each
  * basic type of comparison (equal to, greater than, less than)
  */
public abstract class SelectionCriteria
{
    protected ClassMap map;
    protected String attribute;
    protected Object value;

    public SelectionCriteria(ClassMap map, String attr, Object val)
    {
        this.map = map;
        attribute = new String(attr);
        value = val;
        /*  Tobi:
         *  Generating "SelectionCriteria"-Object, specified by Subclauses later on:
         *   - EqualToCriteria
         *   - NotEqualToCriteria
         *   - GreaterThanCriteria
         *   - GreaterThanEqualCriteria
         *   - LesserThanCriteria
         *   - LesserThanEqualCriteria
         */
    }

    public abstract String asSqlClause();
    /*  Tobi:
     *  Creation and Return of Comparison Clauses like:
     *  " COLUMN = 'VALUE' "
     *  
     *  Clauses are specified by following classes:
     *   - EqualToCriteria
     *   - NotEqualToCriteria
     *   - GreaterThanCriteria
     *   - GreaterThanEqualCriteria
     *   - LesserThanCriteria
     *   - LesserThanEqualCriteria
     *   
     *  Method called by PersistentCriteria.java:
     *   - addSelectEqualTo
     *   - addSelectGreaterThan
     *   - addSelectLessThan
     *   - addSelectNotEqualTo
     */
}