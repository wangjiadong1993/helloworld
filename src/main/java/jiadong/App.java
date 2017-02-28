package jiadong;

/**
 * Hello world!
 *
 */
public class App 
{
	private PortListenerManager portListenerManager;
	private LoggingManager loggingManager;
	public final String LoggingDir = "./log/";
	private ExceptionManager exceptionManager;
	private DatabaseManager databaseManager;
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
}
