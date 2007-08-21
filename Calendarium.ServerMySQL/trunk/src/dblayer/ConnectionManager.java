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
            	  Class.forName("com.mysql.jdbc.Driver");
            	  
            	  /*
            	   * "oracle.jdbc.driver.OracleDriver" for Oracle
            	   * "com.mysql.jdbc.Driver" for MySQL
            	   */

                  Properties DBProps = new Properties();
                  try
                  {
                       FileInputStream in = new FileInputStream("server/connectProperties");
                       /*
                        * "server/OracleConnectProperties" for Oracle
                        * "server/connectProperties" for MySQL
                        */
                       DBProps.load(in);
                       in.close();
                       System.out.print(DBProps.getProperty("Connect"));

                  } 
                  
                  catch(IOException e)
	    	      {
            		    try
            	        {   FileOutputStream out = new FileOutputStream("server/connectProperties");
            	        	/*
            	        	 * "server/OracleConnectProperties" for Oracle
            	        	 * "server/connectProperties" for MySQL
            	        	 */
            	        
            	        	/*
                             * DBProps.save(out, "--- ConnectString Settings ---");
                             * 
                             * the ".save" argument is deprecated and was replaced by ".store"
                             */
                            DBProps.store(out, "--- ConnectString Settings ---");
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
        LogTool log1 = new LogTool("INFO", "ConnectionManager");
    	//log1.engageLogging();
    	
        Statement stmt;
        /*
         * Hier zentrale Stelle, wo SQL-Befehle abgesetzt werden
         * log1.logStatement("Kürzel:" + kuerzel);
         */
        log1.logStatement(sqlString);
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
    	//String sql = "LOCK TABLES " + table + " write";
        //ConnectionManager.processSql(kuerzel,sql);
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