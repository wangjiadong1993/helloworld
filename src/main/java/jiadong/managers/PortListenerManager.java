package jiadong.managers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import jiadong.App;
import jiadong.utils.JsonUtil;
import jiadong.workers.PortListener;
import jiadong.plugins.portListeners.HttpPortListener;
import jiadong.plugins.portListeners.http.Response;
/**
 * Initialized By the App's Main Entry.
 * It will Open the Port for Port Listener.
 * It will Manage Port Listener Destruction.
 * @author wangjiadong
 *
 */
public class PortListenerManager{
	/**
	 * The List Of Ports That Under Management.
	 */
	private static ArrayList<PortListener> portListenerList;
	/**
	 * Singleton
	 */
	public static PortListenerManager portListenerManager;
	/**
	 * Constructor.
	 * @param app, Context
	 * Will Initialize All the Port Listeners.
	 */
	private PortListenerManager() {
		PortListenerManager.portListenerList = new ArrayList<>();
	}
	public static PortListenerManager getInstance(){
		if(PortListenerManager.portListenerManager == null){
			PortListenerManager.portListenerManager = new PortListenerManager();
		}
		return PortListenerManager.portListenerManager;
	}
	/**
	 * Called From the Constructor, to Initialize the Port Listeners.
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void initializePortListeners() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ArrayList<String> plugins = JsonUtil.getStringFromArray(JsonUtil.getJSONArray(JsonUtil.getJSONObject(ResourceManager.CONFIGURATION_OBJECT, "Plugins"), "PortListeners"));
		for(String plugin : plugins){
			LoggingManager.getInstance().log(null, "PortListenerManager:Initializing plugin: "+ plugin);
			Class<?> clazz = Class.forName(ResourceManager.PLUGIN_PACKAGE_NAME+"." + plugin);
			Constructor<?> ctor = clazz.getConstructor(String.class, String.class);
			PortListenerManager.portListenerList.add((PortListener) ctor.newInstance(new Object[] {JsonUtil.getString(JsonUtil.getJSONObject(ResourceManager.CONFIGURATION_OBJECT, plugin), "Port"), JsonUtil.getString(JsonUtil.getJSONObject(ResourceManager.CONFIGURATION_OBJECT, plugin), "Protocol")}));
		}
	}
}
