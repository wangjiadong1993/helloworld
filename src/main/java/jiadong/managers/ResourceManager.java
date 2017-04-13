package jiadong.managers;

import jiadong.utils.FileUtil;
import jiadong.utils.JsonUtil;

import org.json.JSONObject;

public class ResourceManager {
	public static final String CONFIGURATION_FILE		=	"/home/jiadong/Develop/PersonalWork/JDServer/resources/json/configuration.js";
	public static final JSONObject CONFIGURATION_OBJECT = 	JsonUtil.getJSON(FileUtil.readFile(CONFIGURATION_FILE));
	public static final String LISTENER_PLUGIN_PACKAGE_NAME		= 	"jiadong.plugins.portListeners";
	public static final String DATABASE_PLUGIN_PACKAGE_NAME		= 	"jiadong.plugins.databases";
	public static final String ROUTINE_FILE				= 	"/home/jiadong/Develop/PersonalWork/JDServer/resources/json/routing.js";
	public static final String CONTROLLER_DIR 			= 	"/home/jiadong/Develop/PersonalWork/JDServer/src/main/java/jiadong/http/mvc/controllers/";
	public static final String HTML_DIR 				= 	"/home/jiadong/Develop/PersonalWork/JDServer/resources/html/";
	public static final String JAVASCRIPT_DIR 			= 	"/home/jiadong/Develop/PersonalWork/JDServer/resources/js";
	public static final String CSS_DIR 					= 	"/home/jiadong/Develop/PersonalWork/JDServer/resources/css";
	public static final String IMG_DIR 					= 	"/home/jiadong/Develop/PersonalWork/JDServer/resources/img";
}
