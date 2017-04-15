package jiadong.plugins.portListeners.http.mvc;

import jiadong.plugins.portListeners.http.ResourceWorker;
import jiadong.workers.Request;

import static jiadong.managers.ResourceManager.HTML_DIR;
public abstract class Controller {
	private  Request request;
	public String getPage(String pageName){
		return ResourceWorker.getInstance().readFile(HTML_DIR + pageName);
	}
	public Controller(){
		
	}
	public void setRequest(Request request){
		this.request = request;
	}
	public Request getRequest(){
		return this.request;
	}
}
