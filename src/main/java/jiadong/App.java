package jiadong;

import jiadong.managers.*;
/**
 * 
 * @author wangjiadong
 *	The Main Entry, And the Context Handler Of the App.
 *	
 *	
 */


public class App 
{
	/**
	 * Port Listener Manager:
	 * Handle All the Port Listeners.
	 * Each Port Listener Will Monitor 1 Port.
	 */
	private PortListenerManager portListenerManager;
	
	/**
	 * Logging Management.
	 * Log is NOT directly sent into console.
	 * It Maybe Directed Into logging files, according to the environment, and configuration.
	 */
	private LoggingManager loggingManager;
	/**
	 * Logging Directory.
	 * TEMPORARY.
	 */
	public final String LoggingDir = "./log/";
	/**
	 * Exception Management.
	 * Will Handle the exceptions.
	 */
	private ExceptionManager exceptionManager;
	/**
	 * Database Management.
	 */
	private DatabaseManager databaseManager;
   
	/**
	 * Main Function.
	 * Main Entry Of the App.
	 * @param args
	 */
	
	public static void main(String[] args )
    {
    	App app = new App();
    	
    	app.loggingManager = new LoggingManager(app);
    	app.loggingManager.log(app, "Logging Manager Started.");
    	
    	app.loggingManager.log(app, "Initializing Port Listener Manger and Port Listeners.");
    	app.portListenerManager = new PortListenerManager(app);
    	app.loggingManager.log(app, "Port Listener Manager Started.");
    	
    	app.databaseManager = new DatabaseManager(app);
    }
    
    /**
     * Setters And Getters
     * 
     * @param plm
     */
    public void setPortListenerManager(PortListenerManager plm){
    	this.portListenerManager = plm;
    }
    public PortListenerManager getPortListenerManager(){
    	return this.portListenerManager;
    }
    
    
    public void setLoggingManager(LoggingManager lm){
    	this.loggingManager = lm;
    }
    public LoggingManager getLoggingManager(){
    	return this.loggingManager;
    }
    
    public void setDatabaseManager(DatabaseManager lm){
    	this.databaseManager = lm;
    }
    public DatabaseManager getDatabaseManager(){
    	return this.databaseManager;
    }
    
    public void setExceptionManager(ExceptionManager lm){
    	this.exceptionManager = lm;
    }
    public ExceptionManager getExceptionManager(){
    	return this.exceptionManager;
    }
}
