package jiadong.managers;

import jiadong.App;

public class LoggingManager extends Manager{
	private String loggingPath;
	private boolean console;
	private boolean logging;
	private static LoggingManager loggingManager;
	
	public LoggingManager(App app) {
		super(app);
		this.loggingPath = app.LoggingDir;
		this.loggingManager = this;
		managerConstructor();
	}
	public static LoggingManager getInstance(){
		return loggingManager;
	}
	@Override
	public void managerDestructor() {
		// TODO Auto-generated method stub
		
	}
	
	public void log(Object obj, String text){
		System.out.println(obj.getClass().toString() + "	" + text);
	}
	@Override
	public void managerConstructor() {
		// TODO Auto-generated method stub
		
	}
}
