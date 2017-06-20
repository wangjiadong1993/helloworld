package jiadong.utils;

import java.io.IOException;
import java.net.Socket;

import jiadong.workers.Request;
import jiadong.workers.SocketWorker;

public class HttpUtil {
	/*
	 * request is compiled Request, with Request Method as Header
	 */
	public static long getLengthUsingHeader(Request request) throws IOException{
		SocketWorker sw = new SocketWorker(request.getSocket());
		return sw.writeToOs(request.getCompiledRequest()).getContentLength();
	}
	public static void setSSLCertification(){
		
	}
}
