/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: AttributeMap, 2.4.1999       //
////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.util.*;

/** AttributeMap provides information about one instance variable (attribute) of a domain class
  * and its corresponding column name in db (if attribute is mapped to atomic db data-type or
  * or to a nested table (then instance of this AttributeMap holds a hashtable of AttributeMap which
  * represents the inner table)
  */
public class AttributeMap implements java.io.Serializable
{
    private static final long serialVersionUID = -6583264143863296904L;
	private String attribute;
    private String column;
    private boolean key;
    private boolean isNested;
    private Hashtable nestedAttributes;

    public AttributeMap(String attrname, String colname, boolean key, boolean isN)
    {
        attribute = attrname;
        column = colname;
        this.key = key;
        isNested = isN;
        nestedAttributes = new Hashtable();
    }

    public AttributeMap(String attrname, String colname)
    {
        attribute = attrname;
        column = colname;
        this.key = false;
        isNested = false;
        nestedAttributes = new Hashtable();
    }

    /** returns corresponding column name
      */
    public String getColumn()
    {
        return column;
    }

    /* returns name of attribute
     */
    public String getAttribute()
    {
        return attribute;
    }

    public boolean isKey()
    {
        return key;
    }

    public boolean isProxy()
    {
        return key;
    }

    public boolean isNested()
    {
        return isNested;
    }

    @SuppressWarnings("unchecked")
	public void setNestedAttribute(AttributeMap am)
    {
        nestedAttributes.put(am.getAttribute(),am);
    }

    public Hashtable getNestedAttributes()
    {   return nestedAttributes;
    }

    public AttributeMap getNestedAttribute(String attribute)
    {   return (AttributeMap) nestedAttributes.get(attribute);
    }


}