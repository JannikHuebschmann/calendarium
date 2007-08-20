/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: EqualToCriteria, 2.4.1999    //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

/** builds sql clause "attribute = value"
  */
public class EqualToCriteria extends SelectionCriteria
{
    public EqualToCriteria(ClassMap map, String attr, Object val)
    {
        super(map,attr,val);
    }

    public String asSqlClause()
    {
        if (value.getClass().getName().equals("java.lang.String"))
            return new String(map.getAttribute(attribute).getColumn() + " = '"+value+"'");
        else
            return new String(map.getAttribute(attribute).getColumn() + " = "+value+"");
    }
}