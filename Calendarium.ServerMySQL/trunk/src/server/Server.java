package server; //
//////////////////

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import java.util.Properties;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import server.remote.AntwortSetRemote;
import server.remote.DBSetRemote;
import server.remote.FeiertagSetRemote;
import server.remote.GroupSetRemote;
import server.remote.PersonSetRemote;
import server.remote.RightSetRemote;
import server.remote.TerminSetRemote;
import server.remote.ToDoSetRemote;
import server.remote.TypSetRemote;
import dblayer.ClassMappings;

////////////////////////////////////////////////////////////////////////////////////////////
// Server // Server // Server // Server // Server // Server // Server // Server // Server //
////////////////////////////////////////////////////////////////////////////////////////////
public class Server
{
    // Properties
    private Properties properties;

	// MessageServer
	private MessageServer msgServer;

	// Sets
	private PersonSetRemote personen;
	private GroupSetRemote gruppen;
	private TypSetRemote typen;
	private FeiertagSetRemote feiertage;
	private RightSetRemote rechte;
	private TerminSetRemote termine;
	private ToDoSetRemote toDo;
	private AntwortSetRemote antwort; //johnny
	private DBSetRemote dbremote;//johnny um beim monitor zu sehen ob db oder net

    // NfktQueue
    private NfktQueue nfktQueue;

	public Server() throws RemoteException
    {
        properties = getProperties();

        String hostname =  "rmi://" + (String) properties.get("RMIServerName")
                              + ':' + (String) properties.get("RMIServerPort") + '/';

		// European Central Time (ECT)
		if(TimeZone.getDefault().getID() != "ECT")
		{
			// Greenwich Meen Time (GMT) + 1
			SimpleTimeZone tz = new SimpleTimeZone(2 * 60 * 60 * 1000, "GMT");//2 statt 1 wegen mitteleuropäische zeit
			TimeZone.setDefault(tz);
		}

		System.out.println("Serverstart: " + new Date());

        ClassMappings.fillMappings();
        starten(hostname);
    }

    private Properties getProperties()
    {
        Properties defaultProps = new Properties();
        InetAddress thisMachine = null;

        try
	    {   thisMachine = InetAddress.getLocalHost();

        } catch(Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }

        // DefaultsProperties
        defaultProps.put("RMIServerName", thisMachine.getHostName());
	    defaultProps.put("RMIServerPort", "2005");
        defaultProps.put("SMTPServerName", "mailsrv.uni-klu.ac.at");
        defaultProps.put("SMTPServerPort", "25");
		defaultProps.put("CalendariumEMail", "Calendarium");

        // UserProperties
        Properties applicationProps = (Properties) defaultProps.clone();

        try
        {   FileInputStream in = new FileInputStream("server/serverProperties");
            applicationProps.load(in);
            in.close();

			applicationProps.put("RMIServerName", thisMachine.getHostName());

        } catch(IOException e)
		{
    		try
    	    {   FileOutputStream out = new FileOutputStream("server/serverProperties");
                applicationProps.save(out, "--- Server Settings ---");
                out.close();

            } catch(IOException ex) {}
        }

		return applicationProps;
    }

    private void starten(String name)
    {
        // Security Manager runtime
        System.setSecurityManager(new RMISecurityManager());

        // Port
        int port = Integer.valueOf((String) properties.get("RMIServerPort")).intValue();

        try
        {   // Registry starten
            LocateRegistry.createRegistry(port);

            // NfktQueue
            nfktQueue = new NfktQueue(this);

			// Sets
            personen = new PersonSetRemote(this, name);
            gruppen = new GroupSetRemote(this, name);
            typen = new TypSetRemote(this, name);
            feiertage = new FeiertagSetRemote(this, name);
            rechte = new RightSetRemote(this, name);
            termine = new TerminSetRemote(this, name);
            toDo = new ToDoSetRemote(this, name);
            antwort = new AntwortSetRemote(this, name);
            dbremote = new DBSetRemote(this, name);//starten von db service (nur fuer monitor)

			// MessageServer
            msgServer = new MessageServer(this, name);

            getServices(name);

        } catch(Exception e)
        {   e.printStackTrace();
        }
    }

    private void getServices(String name)
    {
        System.out.println("Active Services:");

        try
        {   String services[] = Naming.list(name);

            for(int i = 0; i < services.length; i++)
            {   System.out.println(services[i]);
            }
        } catch(Exception e) {}
    }

	  public MessageServer getMessageServer()
	  {	return msgServer;
	  }

	
	public DBSetRemote getDBSetRemote(){
		return dbremote;
	}
    public PersonSetRemote getPersonSetRemote()
    {   return personen;
    }

    public GroupSetRemote getGroupSetRemote()
    {   return gruppen;
    }

    public TypSetRemote getTypSetRemote()
    {   return typen;
    }

    public FeiertagSetRemote getFeiertagSetRemote()
    {   return feiertage;
    }

	public RightSetRemote getRightSetRemote()
	{   return rechte;
	}

	public TerminSetRemote getTerminSetRemote()
	{   return termine;
	}

	public ToDoSetRemote getToDoSetRemote()
	{   return toDo;
	}

    public NfktQueue getNfktQueue()
    {   return nfktQueue;
    }

    public Properties getServerProperties()
    {   return properties;
    }

	public String getEMailAdresse()
	{	return (String) properties.get("CalendariumEMail");
	}

    public static void main(String[] args)
    {
        try
        {
        	System.setProperty("java.security.policy", "java.policy");//jt
           Server server = new Server();
        } catch(Exception e)
        {   e.printStackTrace();
        }
    }
}