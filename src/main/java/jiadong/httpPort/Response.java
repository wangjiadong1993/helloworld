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
	
	/**
	 * 
	 * 
	 */
	class StatusCode{
		HashMap<Integer, String> codeStatusMap;
		
		
		StatusCode(){
			codeStatusMap = new HashMap<Integer, String>(){/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			{
				put(100, "Continue");
				put(200, "OK");
				put(201, "Created");
				put(204, "No Content");
				put(304, "Not Modified");
				put(400, "Bad Request");
				put(401, "Unautorized");
				put(403, "Forbidden");
				put(404, "Not Found");
				put(409, "Conflict");
				put(500, "Internal Server Error");
			}};
		}
		String getStatusByCode(Integer code){
			if(code == null){
				return null;
			}else{
				if(codeStatusMap.containsKey(code)){
					return codeStatusMap.get(code);
				}else{
					return null;
				}
			}
		}
	}
	
	public Response(Integer code){
		this.headerLine = "HTTP/1.1 " + code.toString() + " "+(new StatusCode()).getStatusByCode(code);
		this.timeStamp = ZonedDateTime.now(ZoneOffset.UTC);
		this.server = "JDServer (Based On Java SE8)";
		this.lastModified = ZonedDateTime.now(ZoneOffset.UTC);
		extraHeaderFields = new HashMap<>();
	}
	
	public Response(Integer code, String message, String messageType){
		this(code);
		this.setMessage(message, messageType);
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
								"Date: " + this.timeStamp + "\r\n" +
								"Server: " + this.server + "\r\n" +
								"Last-Modified： " + this.lastModified + "\r\n" +
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
