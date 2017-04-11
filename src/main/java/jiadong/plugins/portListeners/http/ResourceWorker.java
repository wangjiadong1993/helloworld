package jiadong.plugins.portListeners.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import jiadong.managers.LoggingManager;
import jiadong.utils.FileUtil;

public class ResourceWorker {
	private static ResourceWorker resourceWorker;
	/**
	 * Singleton Getter
	 * @return
	 */
	public static ResourceWorker getInstance(){
		if(resourceWorker == null){
			resourceWorker = new ResourceWorker();
		}
		return resourceWorker;
	}
	
	/**
	 * File Reader
	 * @param path
	 * @return
	 */
	public String readFile(String path){
		return FileUtil.readFile(path);
	}
	
	/**
	 * Resource Getter for JS, images, CSS, and etc.
	 */
	public String readResource(Request request){
		return null;
	}

	public Response readCSSResource(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response readMediaResource(Request request) {
		// TODO Auto-generated method stub
		return null;
	}


	public Response readJSResource(Request request) {
		// TODO Auto-generated method stub
		return null;
	}
}
