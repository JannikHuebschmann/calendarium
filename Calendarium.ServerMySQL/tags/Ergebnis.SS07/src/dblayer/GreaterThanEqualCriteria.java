//////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: GreaterThanCriteria, 2.4.1999 //
//////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

/** builds sql clause "attribute > value"
  */
public class GreaterThanEqualCriteria extends SelectionCriteria
{
    public GreaterThanEqualCriteria(ClassMap map, String attr, Object val)
    {
        super(map,attr,val);
        /*  Tobi:
         *  Generating "GreaterThanEqualCriteria"-Object as specified in SelectionCriteria.java
         */
    }

    public String asSqlClause()
    {
        if (!value.getClass().getName().equals("java.lang.String"))
            return new String(map.getAttribute(attribute).getColumn() + " >= "+value+"");
        else
            return new String(map.getAttribute(attribute).getColumn() + " >= '"+value+"'");
        /*  Tobi:
         *  Generating String as: COLUMN >= 'VALUE'
         */
    }
}