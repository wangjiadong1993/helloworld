package jiadong.managers;

public class LoggingManager{
	private static String loggingPath;
	private static boolean console;
	private static boolean logging;
	private static LoggingManager loggingManager;
	
	private LoggingManager(){
	}
	public static LoggingManager getInstance(){
		if(LoggingManager.loggingManager == null){
			LoggingManager.loggingManager = new LoggingManager();
		}
		return LoggingManager.loggingManager;
	}
	public void managerDestructor() {
		
	}
	
	public void log(Object obj, String text){
		if(obj != null){
			System.out.println(obj.getClass().toString() + "	" + text);
		}else{
			System.out.println(text);
		}
		
	}
}
