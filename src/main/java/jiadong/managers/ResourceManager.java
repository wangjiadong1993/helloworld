package jiadong.managers;

import jiadong.utils.FileUtil;
import jiadong.utils.JsonUtil;

import org.json.JSONObject;

public class ResourceManager {
	public static final String BASE_DIR 				= 	"/Users/wangjiadong/Documents/Development/Eclipse/helloworld/";
	public static final String CONFIGURATION_FILE		=	BASE_DIR + "resources/json/configuration.js";
	
	public static final String ROUTINE_FILE				= 	BASE_DIR + "resources/json/routing.js";
	public static final String CONTROLLER_DIR 			= 	BASE_DIR + "src/main/java/jiadong/http/mvc/controllers/";
	public static final String HTML_DIR 				= 	BASE_DIR + "resources/html/";
	public static final String JAVASCRIPT_DIR 			= 	BASE_DIR + "resources/js";
	public static final String CSS_DIR 					= 	BASE_DIR + "resources/css";
	public static final String IMG_DIR 					= 	BASE_DIR + "resources/img";
	
	public static final JSONObject CONFIGURATION_OBJECT 		= 	JsonUtil.getJSON(FileUtil.readFile(CONFIGURATION_FILE));
	public static final String LISTENER_PLUGIN_PACKAGE_NAME		= 	"jiadong.plugins.portListeners";
	public static final String DATABASE_PLUGIN_PACKAGE_NAME		= 	"jiadong.plugins.databases";
}
