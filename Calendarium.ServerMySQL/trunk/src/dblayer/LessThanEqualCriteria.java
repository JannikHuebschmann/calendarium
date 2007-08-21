/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: LessThanCriteria 2.4.199     //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

/** builds sql clause "attribute < value"
  */
public class LessThanEqualCriteria extends SelectionCriteria
{
    public LessThanEqualCriteria(ClassMap map, String attr, Object val)
    {
        super(map,attr,val);
        /*  Tobi:
         *  Generating "LessThanEqualCriteria"-Object as specified in SelectionCriteria.java
         */
    }

    public String asSqlClause()
    {
        if (!value.getClass().getName().equals("java.lang.String"))
            return new String(map.getAttribute(attribute).getColumn() + " <= "+value+"");
        else
            return new String(map.getAttribute(attribute).getColumn() + " <= '"+value+"'");
        /*  Tobi:
         *  Generating String as: COLUMN <= 'VALUE'
         */
    }
}
