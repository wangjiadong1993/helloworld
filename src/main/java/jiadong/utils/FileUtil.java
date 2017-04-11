package jiadong.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jiadong.managers.LoggingManager;

public class FileUtil {
	public static String readFile(String path){
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
			LoggingManager.getInstance().log(null, "FileUtil::Resource File Not Found. " + e);
		} catch (IOException e) {
			LoggingManager.getInstance().log(null, "FileUtil::IO Exception Ecncountered During File Reading. " + e);
		}
		return output;
	}
}
