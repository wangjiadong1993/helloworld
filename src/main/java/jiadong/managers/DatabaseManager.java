package jiadong.managers;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import jiadong.utils.FileUtil;
import jiadong.utils.JsonUtil;
import jiadong.workers.database.DatabaseAdaptor;

import static jiadong.managers.ResourceManager.DATABASE_PLUGIN_PACKAGE_NAME;

public class DatabaseManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -587791322653310007L;
	private static DatabaseManager databaseManager;
	private static HashMap<Long, DatabaseAdaptor<?>> databaseAdaptors = new HashMap<>();
	private DatabaseManager() {
	}
	public static DatabaseManager getInstance(){
		if(DatabaseManager.databaseManager == null){
			DatabaseManager.databaseManager = new DatabaseManager();
		}
		return DatabaseManager.databaseManager;
	}
	public DatabaseAdaptor<?> getAdaptor(Long uid){
		if(uid == null){
			return null;
		}
		if(DatabaseManager.databaseAdaptors.containsKey(uid)){
			return DatabaseManager.databaseAdaptors.get(uid);
		}
		return null;
	}
	public DatabaseAdaptor<?> createAdaptor(Long uid, String path) throws NoSuchFieldException{
		DatabaseAdaptor<?> da = getAdaptor(uid);
		if(path == null){
			return null;
		}else if(da != null){
			return da;
		}else if(!FileUtil.checkExist(path)){
			return null;
		}else{
			String db_name = JsonUtil.getString(JsonUtil.getJSON(FileUtil.readFile(path)), "Type");
			//claz loader
			ArrayList<String> adaptors = JsonUtil.getStringFromArray(JsonUtil.getJSONArray(JsonUtil.getJSONObject(ResourceManager.CONFIGURATION_OBJECT, "Plugins"), "Databases"));
			
			for(String adaptor: adaptors){
				try {
					Class<?> tmp = Class.forName(DATABASE_PLUGIN_PACKAGE_NAME +"." + adaptor);
					String name;
					try {
						name = (String) tmp.getField("Database_Identifier").get(null);
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw e;
					}
					if(name.equals(db_name)){
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
