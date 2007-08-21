package dblayer;

import java.util.logging.*; 

/** LogTool is intended to provide the possibility for logging
 *  statements, especially those for database operations.
 * 
 * @author Tobias Weiand
 * @version 1.2<P>
 * 
 * changes since last version:<BR>
 * • it is now possible to set a log level while creating a LogTool instance<P>
 * 
 * features in planned version:<BR>
 * • setting handlers for different outputs of messages (console vs. file) 
 * 
 */ 

public class LogTool 
{ 
	/**
	 * logger is the logging object for Database Statements
	 */
	private static final Logger logger = Logger.getLogger( "Logging" );
	
	/**
	 * lever is the indicator whether Logmode is switched on or off
	 */
	private boolean lever=false;
	
	/**
	 * StatementType is optional for differentiating logged messages
	 */
	private String StatementType;
	
	/**
	 * LogLevel is the message level i.e. INFO, WARNING,...
	 */
	private String LogLevel;
	
	/**
	 * Constructor with StatementType and LogLevel<BR>
	 * @param LogLevel should be one of those: ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING
	 * <B>NOTICE: Logmode is not yet switched on</B>
	 * @see #engageLogging()
	 */
	public LogTool(String LogLevel, String StatementType) 
	{ 
		this.StatementType=StatementType;
		this.LogLevel=LogLevel;
	} 
	
	/**
	 * Constructor without StatementType but with LogLevel<BR>
	 * LogLevel should be one of those: ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING<BR><B>
	 * 
	 * NOTICE: Logmode is not yet switched on</B>
	 * @see #engageLogging()
	 */
	public LogTool(String LogLevel) 
	{ 
		this.LogLevel=LogLevel;
	} 
	
	
	/**
	 * logStatement delivers a user definded message to the logger
	 * @param msg is a user defined message
	 */
	public void logStatement(String msg)
	{
		boolean KnownLevel=false;
		String [] LogLevels = {"ALL","CONFIG","FINE","FINER","FINEST","INFO","OFF","SEVERE","WARNING"};
		
		/* 
		 * the array LogLevel includes known/allowed log levels.
		 * if the given LogLevel equals one of the allowed levels, it can be used.
		 * else LogLevel will be set to INFO.
		 */
		
		for(int i=0; i<LogLevels.length; i++)
			if (LogLevel==LogLevels[i])
				KnownLevel=true;
		
		if(!KnownLevel)
				LogLevel="INFO";
		
		if(this.lever==true)
		{
			if(this.StatementType!=null)
				logger.log(Level.parse(LogLevel), StatementType+": "+msg);
			else
				logger.log(Level.parse(LogLevel), msg);
		}
	}
  
	/**
	 * engages Logmode
	 */
	public void engageLogging()
  	{
		this.lever=true;
  	}
	
	/**
	 * disengages Logmode
	 */
	public void disengageLogging()
	{
		this.lever=false;
	}
}