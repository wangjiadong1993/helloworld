package jiadong.plugins.portListeners.http.mvc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import jiadong.managers.DatabaseManager;
import jiadong.managers.LoggingManager;
import jiadong.workers.DBResult;
import jiadong.workers.DatabaseAdaptor;
import jiadong.workers.MinimisedObject;

public abstract class Model<T extends Model<T>> implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -7147743627046243795L;
	public final String 	_created;
	public final String 	_modified;
	public final String 	_deleted;
	public 			Long 	_id;
	private DatabaseAdaptor<?> adaptor = null;
	protected Model() {
		this._created  = ZonedDateTime.now(ZoneOffset.UTC).toString();
		this._modified = ZonedDateTime.now(ZoneOffset.UTC).toString();
		this._deleted = null;
		try {
			adaptor = DatabaseManager.getInstance().createAdaptor(serialVersionUID, "/home/jiadong/Develop/PersonalWork/JDServer/resources/json/dbConfig.js");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public final T insert() throws IllegalArgumentException, IllegalAccessException{
		DBResult result = adaptor.insert(this.getSerializedModel());
		return getModel(result);
	}
	public final void delete(){
		adaptor.delete(this._id);
	}
	public final T update(String key, Object value){
		return null;
	}
	public final T find(Long id) throws IllegalArgumentException, IllegalAccessException{
		Field f = Arrays.asList(this.getClass().getFields()).stream()
								.filter(field -> field.getName() == "_id")
								.collect(Collectors.toList()).get(0);
		DBResult result = adaptor.find(new MinimisedObject(f, this), this.getClass());
		return getModel(result);
	}
	public ArrayList<MinimisedObject> getSerializedModel() throws IllegalArgumentException, IllegalAccessException{
		List<Field> al_f = Arrays.asList(this.getClass().getFields()).stream()
										.filter(field -> field.getName().startsWith("_"))
										.collect(Collectors.toList());
		ArrayList<MinimisedObject> al = new ArrayList<>();
		for(Field field : al_f){
			LoggingManager.getInstance().log(this, "The field is: "+ field.getName());
			al.add(new MinimisedObject(field, this));
		}
		return al;
	}
	public T getModel(DBResult r) {
		return null;
	}
}
