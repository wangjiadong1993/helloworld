package jiadong;

public abstract class Manager {
	protected final App app;
	protected final LoggingManager loggingManager;
	public abstract void managerDestructor();
	public abstract void managerConstructor();
	public Manager(App app){
		this.app = app;
		this.loggingManager = app.getLoggingManager();
	}
}
