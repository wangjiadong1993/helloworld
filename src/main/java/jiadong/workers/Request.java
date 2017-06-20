package jiadong.workers;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

public class Request {
	/**
	 *  http://www.google.com:123/v1/v2/v3?q=helloworld#segment1
	 *  
	 */
	
	private String headerStr;
	private String messageStr;
	/**
	 * HeaderLine
	 */
	public final String requestMethod;
	/**
	 *  http://www.google.com:123/v1/v2/v3?q=helloworld#segment1
	 *  
	 */
	public final String uri;
	
	
	/**
	 *  /v1/v2/v3?q=hello#segment1
	 *  
	 */
	public final String subUri;
	/**
	 *  #segment1
	 *  
	 */
	public String fragment;
	/**
	 *  q=hello
	 *  
	 */
	public HashMap<String, String> params;
	public final String protocolType;
	public final String protocolVersion;
	/**
	 * Headers
	 */
	/**
	 *  www.google.com
	 *  
	 */
	public final String host;
	/**
	 * 123
	 */
	public String port;
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
	 * JSON Message
	 */
//	private JSONObject messageJson;
	/**
	 * Generic Header Types
	 */
	private HashMap<String, String> headerFields = new HashMap<>();
	/**
	 * Constructor
	 */
	public Request(String url, String method){
		this(url, method, "");
	}
	
	/**
	 * 
	 */
	public Request(String url, String method, String msg){
		this.requestMethod = method;
		if(url.toLowerCase().startsWith("http://")){
			this.uri = url;
			this.port = "80";
			this.protocolType = "HTTP";
			this.protocolVersion = "1.1";
		}else if(url.toLowerCase().startsWith("https://")){
			this.uri = url;
			this.port = "443";
			this.protocolType = "HTTPS";
			this.protocolVersion = "1.1";
		}else{
			this.port = "80";
			this.uri = "http://" + url;
			this.protocolType = "HTTP";
			this.protocolVersion = "1.1";
		}
		
		String tmp = this.uri;
		tmp = tmp.substring(tmp.indexOf("://")+3);
		if(!tmp.contains(":")){
			if(!tmp.contains("/")){
				if(!tmp.contains("?")){
					if(!tmp.contains("#")){
						this.host = tmp;
						this.subUri = "/";
					}else{
						this.host = tmp.substring(0, tmp.indexOf("#"));
						this.subUri = "/"+ tmp.substring(tmp.indexOf("#"));
					}
				}else{
					this.host = tmp.substring(0, tmp.indexOf("?"));
					this.subUri = "/"+ tmp.substring(tmp.indexOf("?"));
				}
			}else{
				this.host = tmp.substring(0, tmp.indexOf("/"));
				this.subUri = tmp.substring(tmp.indexOf("/"));
			}
		}else{
			this.host =tmp.substring(0, tmp.indexOf(":"));
			Scanner sc = new Scanner(tmp.substring(tmp.indexOf(":")));
			this.port = String.valueOf(sc.nextInt());
			if(sc.hasNext()){
				this.subUri = sc.next();
			}else{
				this.subUri = "/";
			}
			sc.close();
		}
		
		this.connection = "keep-alive";
		this.origin = "";
		this.userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36";
		this.contentType = "*/*";
		this.accept = "*/*";
		this.contentLength = String.valueOf(msg.length());
				
		this.messageStr = msg;
		
		this.setFragment();
		this.setParams();
	}
	public void setFragment(){
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
	}
	public void setParams(){
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
	}
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
		
		//The HeaderLine GET HTTP/1.1
		String requestLine = header.remove(0);
		String[] headerLineOutput = requestLine.split("\\s+");
		this.requestMethod= headerLineOutput[0];
		this.uri = headerLineOutput[1];
		this.port = null;
		this.subUri = this.uri;
		setFragment();
		setParams();
		
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
	public void setHeader(String key, String value){
		this.headerFields.put(key, value);
	}

	public String compiledHeader(){
		this.headerStr = this.requestMethod+" " + this.subUri + " HTTP/1.1" + "\r\n";
		this.headerStr += ("Host: "+ this.host+ "\r\n");
		
		this.headerStr += ("Connection: "+this.connection + "\r\n");
//		this.headerStr += ("Origin: "+this.origin + "\r\n");
		this.headerStr += ("Accept: "+this.accept + "\r\n");
		this.headerStr += ("User-Agent: "+this.userAgent + "\r\n");
		this.headerStr += ("Content-Type: "+this.contentType + "\r\n");
//		this.headerStr += ("Accept-Language: en-US,en;q=0.8,zh-CN;q=0.6,zh-TW;q=0.4\r\n");
		this.headerStr += ("Content-Length: "+this.contentLength + "\r\n");
		for(Entry<String, String> e : this.headerFields.entrySet()){
			this.headerStr += (e.getKey()+": " + e.getValue() + "\r\n");
		}
		this.headerStr += "\r\n";
		return this.headerStr;
	}
	public String getCompiledRequest(){
		return compiledHeader() + this.messageStr;
	}
	
	
	/*
	 * Integrate the socket inside request
	 */
	public Socket getSocket() throws NumberFormatException, UnknownHostException, IOException{
		if(this.protocolType == "HTTP"){
			return new Socket(this.host, Integer.parseInt(this.port));
		}else if(this.protocolType == "HTTPS"){
			System.setProperty("javax.net.ssl.keyStore","/home/jiadong/.ssh/keys/clientkeystore");
			System.setProperty("javax.net.ssl.keyStorePassword","123!@#qweASD");
		    SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		    System.out.println(this.getCompiledRequest());
		    Socket socket = ssf.createSocket(this.host, Integer.parseInt(this.port));
		    return socket;
		}else{
			return null;
		}
	}
}
