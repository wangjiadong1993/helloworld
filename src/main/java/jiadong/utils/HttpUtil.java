package jiadong.utils;

import java.io.IOException;
import java.net.Socket;

import jiadong.workers.SocketWorker;

public class HttpUtil {
	/*
	 * request is compiled Request, with Request Method as Header
	 */
	public static long getLengthUsingHeader(String ipAddress, int port, String request) throws IOException{
		return (new SocketWorker(new Socket(ipAddress, port))).writeToOs(request).getContentLength();
	}
}
