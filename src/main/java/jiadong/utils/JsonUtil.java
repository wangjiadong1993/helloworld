package jiadong.utils;

import java.util.ArrayList;

import org.json.*;
public class JsonUtil {
	public static JSONObject getJSON(String string){
		return new JSONObject(string);
	}
	
	public static JSONArray getJSONArray(JSONObject object, String key){
		return object.getJSONArray(key);
	}
	public static JSONObject getJSONObject(JSONObject object, String key){
		return object.getJSONObject(key);
	}
	public static String getString(JSONObject object, String key){
		return object.getString(key);
	}
	public static int getInteger(JSONObject object, String key){
		return object.getInt(key);
	}
	
	public static JSONArray getJSONArray(JSONArray object, int count){
		return object.getJSONArray(count);
	}
	public static JSONObject getJSONObject(JSONArray object, int count){
		return object.getJSONObject(count);
	}
	public static String getString(JSONArray object, int count){
		return object.getString(count);
	}
	public static int getInteger(JSONArray object, int count){
		return object.getInt(count);
	}
	public static ArrayList<String> getStringFromArray(JSONArray object){
		ArrayList<String> als = new ArrayList<String>();
		for(int i=0; i< object.length(); i++){
			als.add(object.getString(i));
		}
		return als;
	}
	
	
}
