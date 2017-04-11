package jiadong.workers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import jiadong.App;
import jiadong.managers.LoggingManager;
import jiadong.managers.PortListenerManager;
import jiadong.plugins.portListeners.http.ClientThread;

import java.util.ArrayList;

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
	protected boolean 	keepListen = true;

	
	//Context & Manager
	protected final PortListenerManager portListenerManager;
	
	
	//Server Socket
	protected ServerSocket server;
	//Socket 
	protected Socket 	client;
	//Listen Thread
	private Thread listenThread;
	//Client Thread
	private ArrayList<ClientThread> clientThreadList;
	
	/**
	 * Constructor
	 * @param plm
	 * @param portNum
	 * @param protocolName
	 */
	public PortListener(String portNum, String protocolName){
		
		//Initialization
		this.startDate =new Date();
		this.portListenerManager =PortListenerManager.getInstance();
		this.portNum = Integer.parseInt(portNum);
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
			LoggingManager.getInstance().log(this, "Port Initialization Failed. "+ e);
		}
		LoggingManager.getInstance().log(this, "Port Initialization Succeeded.");
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
