/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: InsertSqlStatement, 2.4.1999 //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;


import java.util.*;

/** builds an insert sql string with the help of ClassMap and its AttributeMaps
  */
public class InsertSqlStatement extends SqlStatement
{   private static final long serialVersionUID = -8341095126717033354L;

	public InsertSqlStatement(ClassMap map, PersistentObject pObj)
    {
        super(map,pObj);
    }

    /** builds insert sql string for one row in db (one instance of a domain class
      * @param attribute not needed
      * @param value not needed
      */
    public void buildForObject(String attribute, Object value)
    {
        Enumeration e;
        int i = 0;
        StringBuffer buf1 = new StringBuffer(),buf2 = new StringBuffer();

        buf.setLength(0);
        e = map.getAttributes().elements();
        while (e.hasMoreElements())
        {
            AttributeMap am = (AttributeMap)e.nextElement();
            if (!am.isNested())
            {
                i++;
                if (i != 1)
                {
                    buf1.append(" , ");
                    buf2.append(" , ");
                }
                buf1.append(am.getColumn());
                String s = object.getValue(am.getAttribute()).getClass().getName();
                if (s.equals("java.lang.String"))
                    buf2.append("'"+object.getValue(am.getAttribute())+"'");
                else
                    buf2.append(""+object.getValue(am.getAttribute())+"");

            }
        }
        buf.append("INSERT INTO " + map.getTablename() + "( ");
        buf.append(new String(buf1));
        buf.append(") VALUES (");
        buf.append(new String(buf2));
        buf.append(")");

    }

    /** builds insert sql string for inner table in one row in db (one instance of a domain class)
      * @param nestedAttr "INSERT INTO THE (SELECT nestedAttr FROM.....
      * @param searchAttr "WHERE searchAttr ="
      * @param val ".... = val"
      * @param pObj "VALUES (pObj.getValue() .....)"
      */
    public void buildForInnerTable(String nestedAttr, PersistentObject pObj, String searchAttr, Object val)
    {
        int i = 0;
        
        LogTool log1 = new LogTool("INFO", "InsertSqlStatement");
        //log1.engageLogging();
                
        log1.logStatement("INNERTABLE: "+map.getTablename()+"; NestedAttribut: "+nestedAttr);
        
        //Analyse parameters (so you are able to call the right insert-sql statement)  
        
        //get the the Enumeration which contains all AttributeMaps. 
        Enumeration e2 = map.getAttribute(nestedAttr).getNestedAttributes().elements();
        AttributeMap am2= (AttributeMap)e2.nextElement();
        if (nestedAttr.equals("nfkt")) 
        {           	
             buf.setLength(0);
             buf.append("INSERT INTO NFKTNESTED(TERMIN_ID,NOTIF_ID) VALUES ");
             buf.append("("+ val + ","+ pObj.getValue(am2.getAttribute()) +")");
            	
        }
        else if ((nestedAttr.equals("gruppen")))
        {
        	buf.setLength(0);
            buf.append("INSERT INTO GROUPNESTED (GROUP_ID,GROUP_ID_CHILD) VALUES ");
            buf.append("("+ val + ","+ pObj.getValue(am2.getAttribute()) +")");
        }
        else if ((nestedAttr.equals("personen")))
        {
        	buf.setLength(0);
            buf.append("INSERT INTO PERSONNESTED (GROUP_ID,PERSON_ID) VALUES ");
            buf.append("("+ val + ","+ pObj.getValue(am2.getAttribute()) +")");
        }
        else if ((nestedAttr.equals("teilnehmer")))
        {
        	buf.setLength(0);
            buf.append("INSERT INTO TEILNEHMERNESTED (TERMIN_ID,TEILNEHMER_ID,NFKT) VALUES ");
            buf.append("("+ val + ","+ pObj.getValue(am2.getAttribute()));
            am2= (AttributeMap)e2.nextElement();
            buf.append(",'"+ pObj.getValue(am2.getAttribute()) +"')");
        }
        else
        	// nested attribute 'personen' not yet implemented
        {
        	
	        buf.setLength(0);
	        buf.append("INSERT INTO THE (SELECT ");
	        buf.append(map.getAttribute(nestedAttr).getColumn());
	        buf.append(" FROM " + map.getTablename() + " WHERE ");
	        buf.append(map.getAttribute(searchAttr).getColumn() + " = ");
	        if (val.getClass().getName().equals("java.lang.String"))
	            buf.append("'"+val+"'");
	        else
	            buf.append(""+val+"");
	        buf.append(") VALUES (");
	
	        Enumeration e = map.getAttribute(nestedAttr).getNestedAttributes().elements();
	        while (e.hasMoreElements())
	        {   
	        	i++;
	            if (i != 1) buf.append(" , ");
	            AttributeMap am = (AttributeMap)e.nextElement();
	            String s = pObj.getValue(am.getAttribute()).getClass().getName();
	                     
	            if (s.equals("java.lang.String"))
	                buf.append("'"+pObj.getValue(am.getAttribute())+"'");
	            else
	                buf.append(""+pObj.getValue(am.getAttribute())+"");
	        }
	        buf.append(")");
        }        
     }

}