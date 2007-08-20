package server; //
//////////////////

import java.io.*;
import java.util.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;



import server.remote.*;

////////////////////////////////////////////////////////////////////////////////////////////
// Server // Server // Server // Server // Server // Server // Server // Server // Server //
////////////////////////////////////////////////////////////////////////////////////////////
/**
 * @author unkown, edit by Ludger Roﬂkamp
 * this class
 */
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
	private AntwortSetRemote antwort;//johnny
    // NfktQueue
    private NfktQueue nfktQueue;
    
    /*/johnny
    public static Server getInstance() {
	    return instance;
	}
    /*/
    /**
     * 
     */
	public Server()
    {   
        properties = getProperties();
        
        String hostname =  "rmi://" + (String) properties.get("RMIServerName") 
                              + ':' + (String) properties.get("RMIServerPort") + '/';
            
		// European Central Time (ECT)
		if(TimeZone.getDefault().getID() != "ECT") 
		{	
			// Greenwich Meen Time (GMT) + 1
			SimpleTimeZone tz = new SimpleTimeZone(2 * 60 * 60 * 1000, "GMT");//2 statt 1 wegen mitteleurop‰ische zeit
			TimeZone.setDefault(tz);			
		}

		System.out.println("Serverstart: " + new Date());
		starten(hostname);
    }
    /**
     * 
     * @return
     */
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
    /**
     * 
     * @param name
     */
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
            antwort=new AntwortSetRemote(this, name);//johnny
           
			// MessageServer
            msgServer = new MessageServer(this, name);

            getServices(name);

        } catch(Exception e)
        {   e.printStackTrace();
        }
    }
    /**
     * this function get Services by name
     * @param name
     */
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
	
	//by johnny
	public AntwortSetRemote getMessageSetRemote(){
		return antwort;
	}

    public NfktQueue getNfktQueue()
    {   return nfktQueue;
    }
    
    public Properties getServerProperties()
    {   return properties;
    }
    
	public String getEMailAdresse()
	{	
		return (String) properties.get("CalendariumEMail");
	}
	
	
	/**
	 * this function is the start function
	 * @param args
	 */
    public static void main(String[] args)
    {
        try
        {  	
        	@SuppressWarnings("unused")
			Server server = new Server();

        } catch(Exception e)
        {   e.printStackTrace();
        }
    }
}