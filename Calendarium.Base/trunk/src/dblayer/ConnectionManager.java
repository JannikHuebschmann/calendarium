package dblayer;       //
/////////////////////////

import java.util.*;
import java.sql.*;
import java.io.*;

//////////////////////////////////////////////////////////////////////////////////////////////////
// ConnectionManager // ConnectionManager // ConnectionManager // ConnectionManager             //
//////////////////////////////////////////////////////////////////////////////////////////////////

public class ConnectionManager
{
	  private static Hashtable connections = new Hashtable();

      public static boolean isActivated()
      {   return (!connections.isEmpty());
      }

	  @SuppressWarnings("unchecked")
	public static void databaseConnection(String kuerzel)
      {
         try
         {
              Connection con = (Connection)connections.get(kuerzel);
              if ((con == null) || (con.isClosed()))
              {
                  Class.forName("oracle.jdbc.driver.OracleDriver");

                  Properties DBProps = new Properties();
                  try
                  {
                       FileInputStream in = new FileInputStream("dblayer/ConnectProperties");
                       DBProps.load(in);
                       in.close();

                  } catch(IOException e)
	    	        {
            		    try
            	        {   FileOutputStream out = new FileOutputStream("dblayer/ConnectProperties");
                            DBProps.save(out, "--- ConnectString Settings ---");
                            out.close();

                        } catch(IOException ex) {}
                    }

                  con = DriverManager.getConnection((String)(DBProps.get("Connect")),(String)(DBProps.get("Benutzer")),(String)(DBProps.get("Passwort")));
                  con.setAutoCommit(false);
                  connections.put(kuerzel,con);

              }
         }
         catch (Exception e) {System.out.println(e);}
    }

    /** execute Sql-Statement and return ResultSet if statement was a select stmt
      */
    public static ResultSet processSql(String kuerzel, String sqlString)
    {
        Statement stmt;

        Connection con = (Connection)connections.get(kuerzel);
        try
        {
            stmt = con.createStatement();
            if (sqlString.indexOf("SELECT") >= 0)
                return stmt.executeQuery(sqlString);
            else
                stmt.executeUpdate(sqlString);
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return null;

    }

    public static void lock(String kuerzel, String table)
    {
        String sql = "LOCK TABLE " + table + " IN EXCLUSIVE MODE";
        ConnectionManager.processSql(kuerzel,sql);
    }



     /** all operations since last commit are committed
      */
    public static void commit(String kuerzel)
    {
        Connection con = (Connection)connections.get(kuerzel);
        try
        {
            con.commit();
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }


    /** undo of all operations since last commit
      */
    public static void rollback(String kuerzel)
    {
        Connection con = (Connection)connections.get(kuerzel);
        try
        {
            con.rollback();
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }


    /** close connection to database
      */
    public static void closeConnection(String kuerzel)
    {
        Connection con = (Connection)connections.get(kuerzel);
        try
        {
            con.close();
            connections.remove(kuerzel);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }



}