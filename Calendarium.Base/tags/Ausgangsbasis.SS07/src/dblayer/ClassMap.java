/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: ClassMap, 2.4.1999           //
////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.util.*;

/** a collection of classes that encapsulate the behavior needed to map classes to db
  */
public class ClassMap implements java.io.Serializable
{
    private static final long serialVersionUID = -7007426934894859730L;
	private String classname;
    private String tablename;
    private Hashtable attributes;

    public ClassMap(String classname, String tablename)
    {
        this.classname = classname;
        this.tablename = tablename;
        attributes = new Hashtable();
    }

    public String getClassname()
    {
        return classname;
    }

    public String getTablename()
    {
        return tablename;
    }

    /** returns AttributeMap for given attribute 'attribute'
      */
    public AttributeMap getAttribute(String attribute)
    {
        return (AttributeMap)attributes.get(attribute);
    }

    /** returns collection of AttributeMap for all attributes of a class
      */
    public Hashtable getAttributes()
    {
        return attributes;
    }

    @SuppressWarnings("unchecked")
	public void setAttributes(AttributeMap am)
    {
        attributes.put(am.getAttribute(),am);
    }

}