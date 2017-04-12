package jiadong.plugins.portListeners.http.mvc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;

public abstract class Model<T extends Model<T>> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String 	created;
	private String 	modified;
	private String 	deleted;
	private Long 	id;
	protected Model(){
		this.created  = ZonedDateTime.now(ZoneOffset.UTC).toString();
		this.modified = ZonedDateTime.now(ZoneOffset.UTC).toString();
		this.deleted = null;
	}
	protected T insert(T model){
		return null;
	}
	protected T delete(T model){
		return null;
	}
	protected T update(long id, HashMap<String, Object> kav){
		return null;
	}
	protected T[] select(){
		return null;
	}
	public String getSerializedModel(){
//		Field[] fields =  this.getClass().getFields();
//		
//		for(Field field : fields){
//			
//		}
		return null;
	}
}
