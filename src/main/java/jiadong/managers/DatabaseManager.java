package jiadong.managers;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import jiadong.utils.FileUtil;
import jiadong.utils.JsonUtil;
import jiadong.workers.DatabaseAdaptor;

public class DatabaseManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -587791322653310007L;
	private static HashMap<Long, DatabaseAdaptor<?>> databaseAdaptors;
	private DatabaseManager() {
	}

	public static DatabaseAdaptor<?> getAdaptor(Long uid){
		if(uid == null){
			return null;
		}
		if(DatabaseManager.databaseAdaptors.containsKey(uid)){
			return DatabaseManager.databaseAdaptors.get(uid);
		}
		return null;
	}
	public static DatabaseAdaptor<?> createAdaptor(Long uid, String path){
		DatabaseAdaptor<?> da = getAdaptor(uid);
		if(path == null){
			return null;
		}else if(da != null){
			return da;
		}else if(!FileUtil.checkExist(path)){
			return null;
		}else{
			String db_name = JsonUtil.getString(JsonUtil.getJSON(FileUtil.readFile(path)), "DatabaseName");
			//claz loader
			ArrayList<String> adaptors = JsonUtil.getStringFromArray(JsonUtil.getJSONArray(JsonUtil.getJSONObject(ResourceManager.CONFIGURATION_OBJECT, "Plugins"), "Databases"));
			
			for(String adaptor: adaptors){
				try {
					Class<?> tmp = Class.forName(adaptor);
					Method method = tmp.getMethod("getIdentifier");
					String name = (String) method.invoke(null);
					if(name == db_name){
						Constructor<?> c = tmp.getConstructor(String.class);
						da = (DatabaseAdaptor<?>) c.newInstance(path);
						databaseAdaptors.put(uid, da);
						return da;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
}
