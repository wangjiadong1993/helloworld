package jiadong.plugins.portListeners.http.mvc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jiadong.managers.LoggingManager;
import jiadong.plugins.portListeners.http.Request;
import jiadong.plugins.portListeners.http.Response;


public class ControllerManager {
	private static ControllerManager controllerManager;
	private final String packageName = "jiadong.plugins.portListeners.http.mvc.controllers.";
	public static ControllerManager getInstance(){
		if(controllerManager == null){
			controllerManager = new ControllerManager();
		}
		return controllerManager;
	}
	public Response callController(String controllerName, String methodName, Request request){
		Object object = null;
		try {
			Class<?> clazz = Class.forName(packageName + controllerName + "Controller");
			Constructor<?> ctor = clazz.getConstructor(Request.class);
			object = ctor.newInstance(request);
			Method m = clazz.getMethod(methodName);
			return (Response) m.invoke(object);
		} catch (ClassNotFoundException e) {
			LoggingManager.getInstance().log(this, "Class Not Found " + e);
		} catch (NoSuchMethodException e) {
			LoggingManager.getInstance().log(this, "Method Not Found " + e);
		} catch (SecurityException e) {
			LoggingManager.getInstance().log(this, "Security Exception  " + e);
		} catch (InstantiationException e) {
			LoggingManager.getInstance().log(this, "Instantiation Exception  " + e);
		} catch (IllegalAccessException e) {
			LoggingManager.getInstance().log(this, "Illegal Access Exception " + e);
		} catch (IllegalArgumentException e) {
			LoggingManager.getInstance().log(this, "Illegal Argument Exception  " + e);
		} catch (InvocationTargetException e) {
			LoggingManager.getInstance().log(this, "Invocation Target Exception  " + e);
		}
		return new Response(500);
		
	}
}
