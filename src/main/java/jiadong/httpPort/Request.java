package jiadong.httpPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;
public class Request {
	public final String headerStr;
	private String messageStr;
	
	public final String requestMethod;
	public final String uri;
	public final String protocolType;
	public final String protocolVersion;
	
	public final String host;
	public final String connection;
	public final String origin;
	public final String userAgent;
	public final String contentType;
	public final String accept;
	public final String contentLength;
	
	public ArrayList<String> acceptEncoding;
	public ArrayList<String> acceptLanguage;
	
	private JSONObject messageJson;
	
	private HashMap<String, String> headerFields;
	/**
	 * Constructor
	 * @param header
	 * @param message
	 */
	public Request(ArrayList<String> header, String message){
		this.messageStr = message;
		this.headerStr = "";
		try{
			messageJson = new JSONObject(message);
		}catch(JSONException e){
			
		}
		String requestLine = header.remove(0);
		Matcher m = Pattern.compile("\\S+").matcher(requestLine);
		this.requestMethod  = m.group(0);
		this.uri = m.group(1);
		m = Pattern.compile("[^/]+").matcher(requestLine);
		this.protocolType = m.group(0);
		this.protocolVersion = m.group(1);
		header.forEach((str) -> {
			Matcher m_ = Pattern.compile("^(\\w+):\\s(.*)$").matcher(str);
			headerFields.put(m_.group(1), m_.group(2));
		});
		this.host = headerFields.get("Host");
		this.connection = headerFields.get("Connection");
		this.origin = headerFields.get("Origin");
		this.contentType = headerFields.get("Content-Type");
		this.userAgent = headerFields.get("User-Agent");
		this.contentLength = headerFields.get("Content-Length");
		this.accept = headerFields.get("Accept");
		m = Pattern.compile("[^;]+").matcher(headerFields.get("Accept-Language"));
		for(int i = 0 ; i< m.groupCount(); i++){
			this.acceptLanguage.add(m.group(i));
		}
		m = Pattern.compile(":\\s(.+,\\s)+$").matcher(headerFields.get("Accept-Encoding"));
		for(int i = 1 ; i< m.groupCount(); i++){
			this.acceptEncoding.add(m.group(i));
		}
	}
	public Request(ArrayList<String> header){
		this(header, "");
	}
	public void setMessageBody(String msg){
		this.messageStr = msg;
		try{
			this.messageJson = new JSONObject(msg);
		}catch(JSONException e){
			
		}
	}
	public JSONObject getMessageJson(){
		return this.messageJson;
	}
	public String getMessageString(){
		return this.messageStr;
	}
	public String getHeaderValueByKey(String key){
		return this.headerFields.get(key);
	}
}
