package jiadong.plugins.portListeners.http.mvc;

import jiadong.plugins.portListeners.http.ResourceWorker;
import static jiadong.managers.ResourceManager.CONTROLLER_DIR;
import static jiadong.managers.ResourceManager.HTML_DIR;
import static jiadong.managers.ResourceManager.JAVASCRIPT_DIR;
import static jiadong.managers.ResourceManager.CSS_DIR;
public abstract class Controller {
	public String getPage(String pageName){
		return ResourceWorker.getInstance().readFile(HTML_DIR + pageName);
	}
	public Controller(){
	}
}
