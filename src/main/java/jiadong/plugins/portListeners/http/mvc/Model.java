package jiadong.plugins.portListeners.http.mvc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Model<T extends Model<T>> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7147743627046243795L;
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
	protected T find(long id){
		return null;
	}
	public ArrayList<MinimisedObject> getSerializedModel() throws IllegalArgumentException, IllegalAccessException{
		Field[] fields =  this.getClass().getFields();
		ArrayList<MinimisedObject> al = new ArrayList<>();
		for(Field field : fields){
			al.add(new MinimisedObject(field.getClass().toString(),field.get(this).toString(), field.getName()));
		}
		return null;
	}
	protected class MinimisedObject{
		String _class;
		String _value;
		String _name;
		MinimisedObject(String c, String v, String n){
			_class = c;
			_value = v;
			_name = n;
		}
	}
}
