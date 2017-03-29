package jiadong.plugins.portListeners.http.mvc;

import jiadong.plugins.portListeners.http.ResourceWorker;


public abstract class Controller {
	private final String CONTROLLER_DIR = 	"/home/jiadong/Develop/PersonalWork/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	private final String HTML_DIR = 			"/home/jiadong/Develop/PersonalWork/JDServer/resources/html/";
	private final String JAVASCRIPT_DIR = 			"/home/jiadong/Develop/PersonalWork/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	private final String CSS_DIR = 			"/home/jiadong/Develop/PersonalWork/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	public String getPage(String pageName){
		return ResourceWorker.getInstance().readFile(HTML_DIR + pageName);
	}
	public Controller(){
		super();
//		LoggingManager.getInstance().log(this, this.getClass().getName() + " Initialized."); 
	}
}
