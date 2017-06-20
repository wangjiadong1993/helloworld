package jiadong.workers;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private byte[] messageBody;
	/**
	 * Request's Response
	 */
	private ArrayList<String> acceptEncoding;
	private ArrayList<String> acceptLanguage;
	private String protocolType;
	private String protocolVersion;
	private int StatusCode;
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
				put(206, "Partial Content");
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
		this.setMessage(message.getBytes(), messageType);
	}
	public Response(Integer code, byte[] message, String messageType){
		this(code);
		this.setMessage(message, messageType);
	}
	
	public Response(String header, byte[] message){
		this.messageBody = message;
		this.extraHeaderFields  = new HashMap<>();
		this.acceptEncoding = new ArrayList<>();
		this.acceptLanguage = new ArrayList<>();
		
		//The HeaderLine GET HTTP/1.1
		List<String> headers = Arrays.asList(header.split("\r\n"));
		String requestLine = headers.remove(0);
		String[] headerLineOutput = requestLine.split("\\s+");	
		this.StatusCode = Integer.parseInt(headerLineOutput[1]);
		headerLineOutput = headerLineOutput[0].split("/");
		this.protocolType= headerLineOutput[0];
		this.protocolVersion = headerLineOutput[1];
		
		//Get Generic Header Types
		headers.forEach((str) -> {
			Matcher m_ = Pattern.compile("^([^:]+):\\s+(.*)$").matcher(str);
			m_.matches();
			String key = m_.group(1);
			String value = m_.group(2);
			this.extraHeaderFields.put(key, value);
		});
		
		//For some special Headers
		this.server = extraHeaderFields.get("Server");
//		this.timeStamp = (new ZonedDateTime()); extraHeaderFields.get("Connection");
//		this.contentLength = extraHeaderFields.get("Origin");
		this.contentType = extraHeaderFields.get("Content-Type");
		this.connection = extraHeaderFields.get("Connection");
		this.contentLength = new Integer(extraHeaderFields.get("Content-Length"));
//		this.accept = extraHeaderFields.get("Accept");
		
		//For some listed headers
		try{
			for(String str : extraHeaderFields.get("Accept-Language").split(";")){
				this.acceptLanguage.add(str);
			}
		}catch(NullPointerException e){
			;
		}
		try{
			for(String str : extraHeaderFields.get("Accept-Encoding").split(",\\s+")){
				this.acceptEncoding.add(str);
			}
		}catch(NullPointerException e){
			;
		}
	}
	public void setMessage(byte[] msg, String msgType){
		this.messageBody = msg;
		this.contentType = msgType;
		this.contentLength = msg.length;
	}
	public byte[] getMessage(){
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
			this.setMessage("".getBytes(), "application/json");
		}
		return this.compoundHeader + this.messageBody;
	}

	public byte[] getByte() {
		if(this.compoundHeader == null){
			this.mergeHeaders();
		}
		if(this.messageBody == null){
			this.setMessage("".getBytes(), "application/json");
		}
		byte[] a = this.compoundHeader.getBytes();
		int aLen = a.length;
		int bLen = this.messageBody.length;
		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(this.messageBody, 0, c, aLen, bLen);
		return c;
	}
}
