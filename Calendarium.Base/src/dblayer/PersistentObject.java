///////////////////////////////////////////////////////////////////////////////////////
//  Simone Stoiber, 9455519, 881, Access Layer Pattern 3: PersistentObject, 4.2.1999 //
///////////////////////////////////////////////////////////////////////////////////////
package dblayer;

import java.sql.ResultSet;
import java.sql.SQLException;


/** all domain objects which need to be persisted must be subclasses of PersistentObject
  */
public abstract class PersistentObject extends Object implements java.io.Serializable
{
    protected boolean isPersistent;     /** identifies whether or not object was ever written to db */
    protected long objectIdentifier;
    protected ClassMap map;
    private OIDManager oidMan;
    private SqlStatement sqlStmt;
    protected String kz;

    public PersistentObject()
    {
        isPersistent = false;
        oidMan = new OIDManager();
    }

    public PersistentObject(long id)
    {
        isPersistent = true;
        objectIdentifier = id;
        oidMan = new OIDManager();
    }

    /** maps one row from db to instance of domain object
      * must be overwritten
      */
    public abstract void swap(ResultSet res);

    /** returns value of class attribute 'attribute'
      * must be overwritten
      */
    public abstract Object getValue(String attribute);

    public void setKz(String k)
    {   kz = k;
    }

    /** get objectIdentifier
      */
    public long getID()
    {   return objectIdentifier;
    }

    /** set objectIdentifier
      */
    public void setID(long id)
    {   objectIdentifier = id;
    }

    public void setPersistence(boolean b)
    {   isPersistent = b;
    }


    protected ClassMap getMapping()
    {
        String c = getClass().getName();
        return ClassMappings.getClass(c.substring(c.lastIndexOf(".")+1,c.length()));
    }

    /** saves PersistentObject to db
      */
    public void save()
    {
        map = getMapping();
        if (!isPersistent)
        {	
            oidMan.setKuerzel(kz);
            objectIdentifier = oidMan.getKey();
            isPersistent = true;

            sqlStmt = new InsertSqlStatement(map,this);
            sqlStmt.buildForObject("objectIdentifier",new Long(objectIdentifier));
        }
        else
        {	
            sqlStmt = new UpdateSqlStatement(map,this);
            sqlStmt.buildForObject("objectIdentifier",new Long(objectIdentifier));
        }
        PersistenceBroker.saveObject(kz,map,sqlStmt.asString());
    }

    /** save persistent object into existing persistent object (database: object is deleted from inner table)
      * @param pObj persistent object that is stored into "this"
      * (eg. users are stored into groups)
      * @param nested_var name of instance variable which represents obj
      */
    public void save(PersistentObject pObj, String nested_var)
    {
        map = getMapping();
        sqlStmt = new InsertSqlStatement(map,this);
        sqlStmt.buildForInnerTable(nested_var,pObj,"objectIdentifier",new Long(objectIdentifier));
        PersistenceBroker.saveObject(kz,map,sqlStmt.asString());
    }

    /** deletes PersistentObject from db
      */
    public void delete()
    {
        map = getMapping();
        sqlStmt = new DeleteSqlStatement(map,this);
        if(isPersistent)
        {
            isPersistent = false;
            sqlStmt.buildForObject("objectIdentifier",new Long(objectIdentifier));
            PersistenceBroker.deleteObject(kz,map,sqlStmt.asString());
        }

    }

    /** delete persistent object into existing persistent object (database: object is written to inner table)
      * @param pObj persistent object that is deleted from "this"
      * (eg. users are deleted from groups)
      * @param nested_var name of instance variable which represents obj
      */
    public void delete(PersistentObject pObj, String nested_var)
    {
        map = getMapping();
        sqlStmt = new DeleteSqlStatement(map,this);
        sqlStmt.buildForInnerTable(nested_var,pObj,"objectIdentifier",new Long(objectIdentifier));
        PersistenceBroker.deleteObject(kz,map,sqlStmt.asString());
    }

    /** loads PersistentObject from db
      * search criteria is the objectIdentifier
      */
    public void retrieve()
    {
        map = getMapping();
        sqlStmt = new SelectSqlStatement(map,this);
        sqlStmt.buildForObject("objectIdentifier",new Long(objectIdentifier));

        ResultSet res = PersistenceBroker.retrieveObject(kz,sqlStmt.asString());
        try
        {
            if (res.next())
            {
                swap(res);
                isPersistent = true;
            }
            else objectIdentifier = 0;  // if object was not found in db set objectIdentifier to "not existent" value
        }catch (SQLException s)
        {
            System.out.println(s);
        }
    }


    /** loads PersistentObject from db
      * @param searchAttr "WHERE search_var = ..." (identifies one row in table)
      * @param value "WHERE ... = value"
      */
    public void retrieve(String search_var, Object value)
    {
        map = getMapping();
        sqlStmt = new SelectSqlStatement(map,this);
        sqlStmt.buildForObject(search_var,value);

        ResultSet res = PersistenceBroker.retrieveObject(kz,sqlStmt.asString());
        try
        {
            if (res.next())
            {
                swap(res);
                isPersistent = true;
            }
            else objectIdentifier = 0;  // if object was not found in db set objectIdentifier to "not existent" value
        }catch (SQLException s)
        {
            System.out.println(s);
        }
    }


    /** loads inner table PersistentObject
      * default search criteria : "WHERE id = objectIdentifier"
      */
    public ResultSet retrieveInnerTable(String nested_var)
    {
        map = getMapping();
        sqlStmt = new SelectSqlStatement(map,this);
        sqlStmt.buildForInnerTable(nested_var,null,"objectIdentifier",new Long(objectIdentifier));

        ResultSet res = PersistenceBroker.retrieveObject(kz,sqlStmt.asString());
        return res;

    }


    /** loads inner table PersistentObject
      * @param nested_var name of inner table
      * @param searchAttr "WHERE searchAttr = ..." (identifies one row in table)
      * @param value "WHERE ... = value"
      */
    public ResultSet retrieveInnerTable(String nested_var, String search_var, Object value)
    {
        map = getMapping();
        sqlStmt = new SelectSqlStatement(map,this);
        sqlStmt.buildForInnerTable(nested_var,null,search_var,value);

        ResultSet res = PersistenceBroker.retrieveObject(kz,sqlStmt.asString());
        return res;

    }


}