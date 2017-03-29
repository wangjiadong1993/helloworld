package jiadong.plugins.portListeners.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jiadong.managers.LoggingManager;

import org.json.JSONException;
import org.json.JSONObject;


public class RoutingWorker {
	private final String dir= "/home/jiadong/Develop/PersonalWork/JDServer/resources/json/routing.js";
	private static RoutingWorker routingWorker;
	private String routingString;
	private JSONObject routingJson;
	private RoutingWorker(){
		this.routingString = ResourceWorker.getInstance().readFile(dir);
		try{
			routingJson = new JSONObject(this.routingString);
		}catch(JSONException e){
			routingJson = null;
		}
		
	}
	
	public static RoutingWorker getInstance(){
		if(routingWorker == null){
			routingWorker = new RoutingWorker();
		}
		return routingWorker;
	}
	
	public void urlResolver(String url){
		Matcher m = Pattern.compile("^.*(\\:\\d)*([^\\?\\/]+)*(\\?([^&]+&)+){0,1}(#(.+)*){0,1}$").matcher(url);
	}
	
	public JSONObject getRoutingJson(){
		return this.routingJson;
	}
	public String getRoute(Request request){
		if(request.accept.toLowerCase().contains("html")){
			String method = request.requestMethod;
			String route = request.uri;
			return getRoute(method, route);
		}else if(request.contentType.toLowerCase().contains("image")){
			return null;
		}else if(request.contentType.toLowerCase().contains("hahahha")){
			return null;
		}else{
			return null;
		}

	}
	String getRoute(String method, String route){
		//getting the uri only, without the suffix params
		Matcher uriMatcher = Pattern.compile("^[\\d\\w\\-\\_\\.\\/]+").matcher(route);
		uriMatcher.matches();
		String uri = uriMatcher.group();
		LoggingManager.getInstance().log(this, "The URI IS " + uri);
		//Translate uri into arraylist
		List<String> uriList = new ArrayList<String>();
		uriList = Arrays.asList(uri.split("\\/")).stream().filter(str -> str.length() >= 1).collect(Collectors.toList());
		LoggingManager.getInstance().log(this, "The URI Segments Are " + uriList.toString());
		
		//if it is /, then simply route it. using /GET or /POST
		if(uriList.size() == 0){
			return this.routingJson.getString("/"+method.toUpperCase());
		//otherwise
		}else{
			//the first segment of uri
			String tmp_str = uriList.remove(0);
			//the routing table
			JSONObject tmp = null;
			try{
				//sub routing table or null
				tmp = this.routingJson.getJSONObject(tmp_str);
			}catch(JSONException e){
				//empty json if key not found
				tmp = null;
			}
			
			//necessary variables;
			Iterator<String> keyIterator;
			String tmp_next;
			HashMap<String, String>  params= new HashMap<String, String>();
			
			//if routing not found, it cannot 
			//1. not routable
			//2. param
			if(tmp == null){
				keyIterator = this.routingJson.keys();
				while(keyIterator.hasNext()){
					tmp_next = keyIterator.next();
					if(tmp_next.startsWith("=")){
						params.put(tmp_next.substring(1),tmp_str);
						LoggingManager.getInstance().log(this, "The Matched JSON Object Is " + this.routingJson.getJSONObject(tmp_next));
						return getRoute(method, uriList, this.routingJson.getJSONObject(tmp_next), params);
					}
				}
				return null;
			}else{
				return getRoute(method, uriList, tmp, params);
			}
		}
	}
	private String getRoute(String method, List<String> uriList, JSONObject partialJson, HashMap<String, String> params ){
		LoggingManager.getInstance().log(this, "Sub Get Route Method: " + method);
		LoggingManager.getInstance().log(this, "Sub Get Route URI List: " + uriList.toString());
		LoggingManager.getInstance().log(this, "Sub Get Route Partial JSON: " + partialJson.toString());
		if(uriList.size() == 0){
			String output =  partialJson.getString("/"+method.toUpperCase());
			LoggingManager.getInstance().log(this, "Sub Output: " + output);
			return output;
		}else{
			String tmp_str = uriList.remove(0);
			JSONObject tmp = null;
			try{
				//sub routing table or null
				tmp = partialJson.getJSONObject(tmp_str);
			}catch(JSONException e){
				//empty json if key not found
				tmp = null;
			}
			Iterator<String> keyIterator;
			String tmp_next;
			if(tmp == null){
				keyIterator = partialJson.keys();
				while(keyIterator.hasNext()){
					tmp_next = keyIterator.next();
					if(tmp_next.startsWith("=")){
						params.put(tmp_next.substring(1),tmp_str);
						LoggingManager.getInstance().log(this, "= Match: " + tmp_next);
						return getRoute(method, uriList, partialJson.getJSONObject(tmp_next), params);
					}
				}
				return null;
			}else{
				return getRoute(method, uriList, tmp, params);
			}
			
		}
	}
}
