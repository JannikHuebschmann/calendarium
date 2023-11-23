package servletPackage;

import swtkal.server.javapersistence.JPAServer;
import swtkal.server.Server;

public class DoGetServer 
{
	private static boolean srvrunning;
	private static JPAServer swtksrv;
	//private static Server swtksrv;
	
    public JPAServer srvobj ()
    //public Server srvobj ()
    {
    	if(!srvrunning)
    	{
    		swtksrv = new JPAServer();
    		//swtksrv = swtkal.server.SimpleServer.getServer();
    		srvrunning = true;
    	}
    	
    	return swtksrv;
    }
}
