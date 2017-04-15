package jiadong.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
	public final String headerStr;
	private String messageStr;
	/**
	 * HeaderLine
	 */
	public final String requestMethod;
	public final String uri;
	public final String fragment;
	public final HashMap<String, String> params;
	public final String protocolType;
	public final String protocolVersion;
	/**
	 * Headers
	 */
	public final String host;
	public final String connection;
	public final String origin;
	public final String userAgent;
	public final String contentType;
	public final String accept;
	public final String contentLength;
	/**
	 * Listed Headers
	 */
	public ArrayList<String> acceptEncoding;
	public ArrayList<String> acceptLanguage;
	/**
	 * Json Message
	 */
//	private JSONObject messageJson;
	/**
	 * Generic Header Types
	 */
	private HashMap<String, String> headerFields;
	/**
	 * Constructor
	 * @param header
	 * @param message
	 */
	public Request(ArrayList<String> header, String message){
		this.messageStr = message;
		this.headerStr = "";
		this.headerFields  = new HashMap<>();
		this.acceptEncoding = new ArrayList<>();
		this.acceptLanguage = new ArrayList<>();
		
//		try{
//			messageJson = new JSONObject(message);
//		}catch(JSONException e){
//			
//		}
		
		//The HeaderLine GET HTTP/1.1
		String requestLine = header.remove(0);
		String[] headerLineOutput = requestLine.split("\\s+");
		this.requestMethod= headerLineOutput[0];
		this.uri = headerLineOutput[1];
		
		if(this.uri.contains("#")){
			String tmp = null;
			try{
				tmp = this.uri.substring(this.uri.indexOf('#')+1);
			}catch(IndexOutOfBoundsException e){
				tmp = null;
			}
			this.fragment = tmp;
		}else{
			this.fragment = null;
		}
		if(this.uri.contains("?")){
			String params = null;
			if(this.uri.contains("#")){
				try{
					params = this.uri.substring(this.uri.indexOf('?')+1, this.uri.indexOf('#'));
				}catch(IndexOutOfBoundsException e){
					params = null;
				}
			}else{
				try{
					params = this.uri.substring(this.uri.indexOf('?')+1);
				}catch(IndexOutOfBoundsException e){
					params = null;
				}
			}
			this.params = new HashMap<String, String>();
			for(String param: params.split("&")){
				String[] kav = param.split("=");
				if(kav.length != 2){
					continue;
				}else{
					this.params.put(kav[0], kav[1]);
				}
			}
		}else{
			this.params = null;
		}
		
		
		headerLineOutput = headerLineOutput[2].split("/");
		this.protocolType= headerLineOutput[0];
		this.protocolVersion = headerLineOutput[1];
		
		//Get Generic Header Types
		header.forEach((str) -> {
			Matcher m_ = Pattern.compile("^([^:]+):\\s+(.*)$").matcher(str);
			m_.matches();
			String key = m_.group(1);
			String value = m_.group(2);
			this.headerFields.put(key, value);
		});
		
		//For some special Headers
		this.host = headerFields.get("Host");
		this.connection = headerFields.get("Connection");
		this.origin = headerFields.get("Origin");
		this.contentType = headerFields.get("Content-Type");
		this.userAgent = headerFields.get("User-Agent");
		this.contentLength = headerFields.get("Content-Length");
		this.accept = headerFields.get("Accept");
		
		//For some listed headers
		try{
			for(String str : headerFields.get("Accept-Language").split(";")){
				this.acceptLanguage.add(str);
			}
		}catch(NullPointerException e){
			;
		}
		
		try{
			for(String str : headerFields.get("Accept-Encoding").split(",\\s+")){
				this.acceptEncoding.add(str);
			}
		}catch(NullPointerException e){
			;
		}
		
	}
	/**
	 * Another Constructor
	 * @param header
	 */
	public Request(ArrayList<String> header){
		this(header, "");
	}
	/**
	 * Setters & Getters
	 * @param msg
	 */
	public void setMessageBody(String msg){
		this.messageStr = msg;
	}
	public String getMessageString(){
		return this.messageStr;
	}
	public String getHeaderValueByKey(String key){
		return this.headerFields.get(key);
	}
}
