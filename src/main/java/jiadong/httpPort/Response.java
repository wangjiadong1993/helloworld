package jiadong.httpPort;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class Response {
	private String headerLine;
	private ZonedDateTime timeStamp;
	private String server;
	private ZonedDateTime lastModified;
	private Integer contentLength = 0;
	private String contentType = "application/json";
	private String connection = "closed";
	private HashMap<String, String> extraHeaderFields;
	private String compoundHeader;
	private String messageBody;
	public Response(Integer code){
		this.headerLine = "HTTP/1.1 " + code.toString() + " OK";
		this.timeStamp = ZonedDateTime.now(ZoneOffset.UTC);
		this.server = "JDServer (Based On Java SE8)";
		this.lastModified = ZonedDateTime.now(ZoneOffset.UTC);
		extraHeaderFields = new HashMap<>();
	}
	public void setMessage(String msg, String msgType){
		this.messageBody = msg;
		this.contentType = msgType;
		this.contentLength = msg.length();
	}
	public String getMessage(){
		return this.messageBody;
	}
	public void setConnection(String connection){
		this.connection = connection;
	}
	public void setExtraHeaderFields(HashMap<String, String> fields){
		for(Entry<String, String> e : fields.entrySet()){
			this.extraHeaderFields.put(e.getKey(), e.getValue());
		}
	}
	public void mergeHeaders(){
		this.compoundHeader = 	this.headerLine + "\r\n" +
//								"Date: " + this.timeStamp + "\r\n" +
//								"Server: " + this.server + "\r\n" +
//								"Last-Modified： " + this.lastModified + "\r\n" +
								"Content-Length: " + this.contentLength + "\r\n" +
								"Content-Type: " + this.contentType + "\r\n" +
								"Connection: " + this.connection + "\r\n" +
								"\r\n";
	}
	public String getResponse(){
		if(this.compoundHeader == null){
			this.mergeHeaders();
		}
		if(this.messageBody == null){
			this.setMessage("", "application/json");
		}
		return this.compoundHeader + this.messageBody;
	}
}
