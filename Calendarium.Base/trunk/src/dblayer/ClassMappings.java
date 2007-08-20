package dblayer;       //
/////////////////////////

import java.util.*;
import java.sql.*;


public class ClassMappings
{
    private static Hashtable classMap = new Hashtable();

    /** return ClassMap instance from given classname 'name'
      */
    public static ClassMap getClass(String name)
    {
        return (ClassMap)classMap.get(name);
    }

    /** fetch mapping from db and store it in corresponding instances of ClassMap
      */
    @SuppressWarnings("unchecked")
	public static void fillMappings()
    {
        ResultSet res,res1;
        ClassMap cm = null;
        String nestedClass, help = null;
        boolean key = false, isNested = false;

        if (!classMap.isEmpty()) return;   // classMap already filled
        try
        {
            ConnectionManager.databaseConnection("Admin");
            res = ConnectionManager.processSql("Admin","SELECT * FROM Calendarium WHERE tablename IS NOT NULL ORDER BY classname");

            if (res == null)    // tables don't exist in db
            {
                DBInit dbInit = new DBInit();
                dbInit.createTables();
                res = ConnectionManager.processSql("Admin","SELECT * FROM Calendarium WHERE tablename IS NOT NULL ORDER BY classname");
            }

            while (res.next())
            {
                if (!res.getString("classname").equals(help))
                {
                    if (cm != null)
                        classMap.put(new String(help),(ClassMap)cm);
                    cm = new ClassMap(res.getString("classname"),res.getString("tablename"));
                }
                key = (res.getInt("key") == 1);
                isNested = (res.getInt("nested") == 1);

                AttributeMap am = new AttributeMap(res.getString("attribute"),res.getString("col"),key,isNested);
                cm.setAttributes(am);

                if (res.getString("nestedClass") != null)
                {
                    nestedClass = res.getString("nestedClass");
                    res1 = ConnectionManager.processSql("Admin","SELECT * FROM Calendarium WHERE classname = '"+nestedClass+"'");
                    while (res1.next())
                    {
                        AttributeMap aam = new AttributeMap(res1.getString("attribute"),res1.getString("col"));
                        am.setNestedAttribute(aam);
                    }
                    res1.close();
                }

                help = res.getString("classname");
            }
            classMap.put(help,(ClassMap)cm);

        }catch (Exception s){System.out.println(s);}
    }

}