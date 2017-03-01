package jiadong.workers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import jiadong.httpPort.ClientThread;
import jiadong.managers.PortListenerManager;

public abstract class PortListener {
	/**
	 * Instance Variables
	 */
	protected final Integer portNum;
	
	protected final String 	protocolName;
	
	
	//Time Stamp For the Listener
	protected final Date 	startDate;
	//Comment For the Listener
	protected String 	comment;

	//A Boolean Variable/Flag
	//The Listener will terminate once turned False;
	protected boolean 	keepListening = true;

	
	//Context & Manager
	protected final PortListenerManager portListenerManager;
	
	
	//Server Socket
	protected ServerSocket server;
	//Socket 
	protected Socket 	client;

	//Multi-Thread 
	ClientThread  clientThread;
	
	
	/**
	 * Constructor
	 * @param plm
	 * @param portNum
	 * @param protocolName
	 */
	public PortListener(PortListenerManager plm, Integer portNum, String protocolName){
		
		//Initialization
		this.startDate =new Date();
		this.portListenerManager = plm;
		this.portNum = portNum;
		this.protocolName = protocolName;
		
		
		//Start Listening.
		this.startListening();
	}
	/**
	 * Start Listening On the designated port.
	 * Called From the Constructor.
	 */
	public void startListening(){
		
		try {
			server = new ServerSocket(this.portNum);
		} catch (IOException e) {
			this.portListenerManager.getLoggingManager().log(this, "Port Initialization Failed. "+ e);
		}
		this.portListenerManager.getLoggingManager().log(this, "Port Initialization Succeeded.");
				
		//started Server Socket.

		while(this.keepListening){
			try {
				//Received A Client Socket.
				client = server.accept();
				//Passing This Client Socket To A New Thread.
				clientThread = new ClientThread(client, this.portListenerManager.getAppContext(), this);
				clientThread.start();
				this.portListenerManager.getLoggingManager().log(this, "Request Acceptance Succeeded.");
			} catch (IOException e) {
				this.portListenerManager.getLoggingManager().log(this, "Port Initialization Failed. "+ e);
			}
		}
	}
	
	/**
	 * Destructor.
	 * Abstract.
	 * To be implemented.
	 */
	public abstract void listenerDestructor();
	
	/**
	 * Getters And Setters.
	 * @return
	 */
	public Integer getPortNum(){
		return this.portNum;
	}
	public String getProtocolName(){
		return this.protocolName;
	}
	public Date getStartDate(){
		return this.startDate;
	}
	public void setComment(String comment){
		this.comment = comment;
	}
	public String getComment(){
		return this.comment;
	}
}
