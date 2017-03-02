package jiadong.http.mvc;

import jiadong.httpPort.ResourceWorker;

public abstract class Controller {
	private final String controllerDir = "/home/jiadong/Downloads_eclipse/JDServer/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	private final String htmlDir = "/home/jiadong/Downloads_eclipse/JDServer/JDServer/resources/html/";
	private final String jsDir = "/home/jiadong/Downloads_eclipse/JDServer/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	private final String cssDir = "/home/jiadong/Downloads_eclipse/JDServer/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	public String getPage(String pageName){
		return ResourceWorker.getInstance().readFile(htmlDir + pageName);
	}
//	public Controller(String input){
//		
//	}
}
