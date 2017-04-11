package jiadong.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jiadong.managers.LoggingManager;

public class FileUtil {
	public static String readFile(String path){
		FileReader fileReader;
		BufferedReader bufferedReader;
		StringBuilder sb = new StringBuilder();
		char[] cbuf = new char[1000];
		try {
			fileReader = new FileReader(new File(path));
			bufferedReader = new BufferedReader(fileReader);
			while(bufferedReader.ready()){
				int tmp = bufferedReader.read(cbuf);
				if(tmp == -1 ){break;}
				sb.append(cbuf, 0, tmp);
			}
			fileReader.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			LoggingManager.getInstance().log(null, "FileUtil::Resource File Not Found. " + e);
		} catch (IOException e) {
			LoggingManager.getInstance().log(null, "FileUtil::IO Exception Ecncountered During File Reading. " + e);
		}
		return sb.toString();
	}
	public static byte[] readBinaryFile(String path) throws IOException{
		return Files.readAllBytes(Paths.get(path));
	}
	public static boolean checkExist(String path){
		File f = new File(path);
		return f.exists();
	}
}
