package jiadong.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import jiadong.App;
import jiadong.managers.LoggingManager;
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
	
	private PortListener portListener;
	//Request Message
	private ArrayList<String> requestMessage;
	/*Constructor
	 * */
	public ClientThread(Socket client, App app, PortListener portListener){
		/**
		 * Context SetUp
		 */
		this.client = client;
		this.app = app;
		this.loggingManager = app.getLoggingManager();
		this.portListener = portListener;
		/**
		 * Request Message Initialization
		 */
		
		this.requestMessage = new ArrayList<>();
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
		/**
		 * string temporary variable,
		 * used to receive string from buffered reader.
		 */
		String str = null;
		/**
		 * A flag used to stop the while loop
		 */
		boolean flag  = true;
		/**
		 * A while loop to receive request header
		 */
		while(flag){
			/**
			 * A buffered reader to read lines
			 * Cannot be used to read messages
			 */
			try {
				str = this.bufferedReader.readLine();
			} catch (IOException e) {
				this.loggingManager.log(this, "Buffered Reader Encountered Error.");
			}
			/**
			 * detect the end of the header
			 */
			if(str == null){
				flag = false;
			}
			if(str.length() == 0){
				flag = false;
			}else{
				//logging and push into request Message.
				this.loggingManager.log(this, str);
				this.requestMessage.add(str);
			}
		}	
		//get the length of the message from request header
//		int contentLength = 0;
//		Request r = new Request(requestMessage, "");
//		try{
//			contentLength  = Integer.parseInt(r.contentLength);
//		}catch(NumberFormatException e){
//			contentLength = 0;
//		}
//		this.loggingManager.log(this, String.valueOf(contentLength));
//		/**
//		 * Read the message from the request
//		 */
//		char[] msg  = new char[contentLength];
//		try {
//			this.bufferedReader.read(msg, 0, contentLength);
//		} catch (IOException e) {
//			this.loggingManager.log(this, "Exception Encountered" + e);
//		}
//		r.setMessageBody(String.copyValueOf(msg));
//		this.loggingManager.log(this,"Result : " + String.copyValueOf(msg));
//		Response response = this.portListener.processRawRequest(r);

		this.printWriter.print((String) null);
		
		this.printWriter.flush();
		
		try {
			this.client.close();
			this.loggingManager.log(this, "Client Socket Closed Successfully");
		} catch (IOException e) {
			this.loggingManager.log(this, "Error Encountered During Socket Closing : " + e);
		}
		try {
			this.inputStreamReader.close();
			this.loggingManager.log(this, "Input Stream Reader Closed Successfully");
		} catch (IOException e) {
			this.loggingManager.log(this, "Error Encountered During Input Stream Reader Closing : " + e);
		}
		this.printWriter.close();
		try {
			this.bufferedReader.close();
			this.loggingManager.log(this, "Buffered Reader Closed Successfully");
		} catch (IOException e) {
			this.loggingManager.log(this, "Error Encountered During Buffered Reader Closing : " + e);
		}
	}
}
