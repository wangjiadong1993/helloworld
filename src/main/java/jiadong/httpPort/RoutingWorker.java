package jiadong.httpPort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;


public class RoutingWorker {
	private final String dir= "/home/jiadong/Downloads_eclipse/JDServer/JDServer/resources/json/routing.js";
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
	public JSONObject getRoutingJson(){
		return this.routingJson;
	}
	public String getRoute(Request request){
		String method = request.requestMethod;
		String route = request.uri;
		return getRoute(method, route);
	}
	String getRoute(String method, String route){
		Matcher uriMatcher = Pattern.compile("^[\\d\\w\\-\\_\\.\\/]+").matcher(route);
		uriMatcher.matches();
		String uri = uriMatcher.group();
		List<String> uriList = new ArrayList<String>();
		uriList = Arrays.asList(uri.split("\\/")).stream().filter(str -> str.length() >= 1).collect(Collectors.toList());
		if(uriList.size() == 0){
			return this.routingJson.getString("/"+method.toUpperCase());
		}else{
			String tmp_str = uriList.remove(0);
			JSONObject tmp = this.routingJson.getJSONObject(tmp_str);
			Iterator<String> keyIterator;
			String tmp_next;
			HashMap<String, String>  params= new HashMap<String, String>();
			if(tmp == null){
				keyIterator = tmp.keys();
				while(keyIterator.hasNext()){
					tmp_next = keyIterator.next();
					if(tmp_next.startsWith("=")){
						params.put(tmp_next.substring(1),tmp_str);
						return getRoute(method, uriList, this.routingJson.getJSONObject(tmp_next), params);
					}
				}
			}
			return null;
		}
	}
	private String getRoute(String method, List<String> uriList, JSONObject partialJson, HashMap<String, String> params ){
		if(uriList.size() == 0){
			return this.routingJson.getString("/"+method.toUpperCase());
		}else{
			String tmp_str = uriList.remove(0);
			JSONObject tmp = this.routingJson.getJSONObject(tmp_str);
			Iterator<String> keyIterator;
			String tmp_next;
			if(tmp == null){
				keyIterator = tmp.keys();
				while(keyIterator.hasNext()){
					tmp_next = keyIterator.next();
					if(tmp_next.startsWith("=")){
						params.put(tmp_next.substring(1),tmp_str);
						return getRoute(method, uriList, this.routingJson.getJSONObject(tmp_next), params);
					}
				}
			}
			return null;
		}
	}
}
