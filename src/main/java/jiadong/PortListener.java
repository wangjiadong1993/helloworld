package jiadong;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public abstract class PortListener {
	protected final Integer portNum;
	protected final String 	protocolName;
	protected final Date 	startDate;
	protected String 	comment;
	protected Socket 	client;
	protected boolean 	keepListening = true;
	protected final PortListenerManager portListenerManager;
	protected ServerSocket server;
	public PortListener(PortListenerManager plm, Integer portNum, String protocolName){
		this.startDate =new Date();
		this.portListenerManager = plm;
		this.portNum = portNum;
		this.protocolName = protocolName;
	}
	public void startListening(){
		try {
			server = new ServerSocket(this.portNum);
		} catch (IOException e) {
			this.portListenerManager.loggingManager.log(this, "Port Initialization Failed. "+ e);
		}
		
		while(this.keepListening){
			try {
				Thread t = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
					}
					
				});
				client = server.accept();
				this.portListenerManager.loggingManager.log(this, "Request Acceptance Succeeded.");
			} catch (IOException e) {
				this.portListenerManager.loggingManager.log(this, "Port Initialization Failed. "+ e);
			}
		}
	}
	public abstract void listenerDestructor();
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
