package jiadong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpUtil {
	/*
	 * request is compiled Request, with Request Method as Header
	 */
	public static long getLengthUsingHeader(String ipAddress, int port, String request) throws IOException{
		Socket socket = new Socket(ipAddress, port);
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		os.write(request.getBytes(), 0, request.length());
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
