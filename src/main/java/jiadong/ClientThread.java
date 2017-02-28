package jiadong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

	//Socket Handler
	private Socket client; 
	
	//Context Handler
	private App app;
	
	//Logging Manager Handler
	private LoggingManager loggingManager;
	
	//Socket IO Stream
	private InputStream inputStream;
	private OutputStream outputStream;
	
	//Socket Reader & Writer
	private InputStreamReader inputStreamReader;
	private PrintWriter printWriter;
	
	//Buffered Reader
	private BufferedReader bufferedReader;
	
	/*Constructor
	 * */
	ClientThread(Socket client, App app){
		/**
		 * Context SetUp
		 */
		this.client = client;
		this.app = app;
		this.loggingManager = app.getLoggingManager();
		
		/**
		 * IO Stream SetUp
		 */
		try {
			this.inputStream = client.getInputStream();
		} catch (IOException e) {
			this.loggingManager.log(this, "Input Stream Failed To Initialize.");
		}
		try {
			this.outputStream = client.getOutputStream();
		} catch (IOException e) {
			this.loggingManager.log(this, "Output Stream Failed To Initialize.");
		}
		
		/**
		 * IO Reader & Writer SetUp
		 */
		this.inputStreamReader = new InputStreamReader(inputStream);
		//auto flush: true
		this.printWriter = new PrintWriter(outputStream, true);
		
		/**
		 * Buffered Reader
		 */
		this.bufferedReader = new BufferedReader(inputStreamReader);
	}
	/**
	 * Multi-Thread Running Logic
	 */
	public void run(){
		String str = null;
		boolean flag  = true;
		while(flag){
			try {
				str = this.bufferedReader.readLine();
			} catch (IOException e) {
				this.loggingManager.log(this, "Buffered Reader Encountered Error.");
			}
			this.loggingManager.log(this, str);
		}
	}
}
