package jiadong.managers;

import jiadong.App;

public class LoggingManager extends Manager{
	private String loggingPath;
	private boolean console;
	private boolean logging;
	
	public LoggingManager(App app) {
		super(app);
		this.loggingPath = app.LoggingDir;
		managerConstructor();
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
