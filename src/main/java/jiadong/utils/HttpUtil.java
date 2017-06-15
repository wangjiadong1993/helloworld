package jiadong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import jiadong.workers.network.Request;

public class HttpUtil {
	public static long getLengthUsingHeader(Request request) throws IOException{
		Socket socket = request.getSocket();
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tmpStr = request.getCompiledRequest();
		os.write(tmpStr.getBytes(), 0, tmpStr.length());
		String tmp = null;
		long length = 0;
		try {
			while(true){
				while(!br.ready());
				tmp = br.readLine();
				if(tmp.startsWith("Content-Length")){
					length = Long.parseLong(tmp.substring(tmp.indexOf(" ")+1));
					socket.close();
					return length;
				}else if(tmp.equals("\r\n")){
					break;
				}else{
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
		return -1;
	}
}
