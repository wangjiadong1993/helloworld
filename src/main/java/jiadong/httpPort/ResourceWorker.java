package jiadong.httpPort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import jiadong.managers.LoggingManager;

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
		FileReader fileReader;
		BufferedReader bufferedReader;
		String output = "";
		try {
			fileReader = new FileReader(new File(path));
			bufferedReader = new BufferedReader(fileReader);
			while(bufferedReader.ready()){
				output += bufferedReader.readLine();
				output += "\r\n";
			}
			fileReader.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			LoggingManager.getInstance().log(this, "Resource File Not Found. " + e);
		} catch (IOException e) {
			LoggingManager.getInstance().log(this, "IO Exception Ecncountered During File Reading. " + e);
		}
		return output;
	}
	
	/**
	 * Resource Getter for JS, images, CSS, and etc.
	 */
	public String readResource(Request request){
		
	}
}
