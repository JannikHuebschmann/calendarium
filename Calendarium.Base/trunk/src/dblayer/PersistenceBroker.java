/////////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: PersistenceBroker, 2.4.1999  //
/////////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.sql.*;

/** maintains connection to database and handles communication between
  * the object application and database
  */
public class PersistenceBroker
{
    /** saves domain object (transformated into a sql insert string) to db
      */
    public static void saveObject(String kuerzel, ClassMap map, String sql)
    {
        try
        {
            ConnectionManager.lock(kuerzel,map.getTablename());
            ConnectionManager.processSql(kuerzel,sql);
            ConnectionManager.commit(kuerzel);

        }catch (Exception e) {System.out.println(e);}
    }

    /** retrieves domain object (transformated into a sql select string) from db
      */
    public static ResultSet retrieveObject(String kuerzel, String sql)
    {
        return ConnectionManager.processSql(kuerzel,sql);
    }

    /** deletes domain object (transformated into a sql delete string) from db
      */
    public static void deleteObject(String kuerzel, ClassMap map, String sql)
    {
        try
        {
            ConnectionManager.lock(kuerzel,map.getTablename());
            ConnectionManager.processSql(kuerzel,sql);
            ConnectionManager.commit(kuerzel);

        }catch (Exception e) {System.out.println(e);}
    }

    /** processes (select) sql string which is "constructed" by the application
      */
    public static ResultSet processSql(String kuerzel, String sql)
    {
        ResultSet res = null;
        try
        {
            res =  ConnectionManager.processSql(kuerzel,sql);
            if (sql.indexOf("SELECT") < 0)    // DML string
                ConnectionManager.commit(kuerzel);
        }catch (Exception e) {System.out.println(e);}

        return res;
    }


  }