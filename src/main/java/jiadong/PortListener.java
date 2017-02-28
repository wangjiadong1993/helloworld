package jiadong;

import java.util.Date;

public abstract class PortListener {
	private final Integer portNum;
	private final String 	protocolName;
	private final Date 	startDate;
	private String 	comment;
	private final PortListenerManager portListenerManager;
	public PortListener(PortListenerManager plm, Integer portNum, String protocolName){
		this.startDate =new Date();
		this.portListenerManager = plm;
		this.portNum = portNum;
		this.protocolName = protocolName;
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
