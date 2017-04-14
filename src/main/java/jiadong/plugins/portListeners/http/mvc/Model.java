package jiadong.plugins.portListeners.http.mvc;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jiadong.managers.DatabaseManager;
import jiadong.managers.LoggingManager;
import jiadong.workers.DatabaseAdaptor;
import jiadong.workers.MinimisedObject;
import static jiadong.managers.ResourceManager.BASE_DIR;

public abstract class Model<T extends Model<T>> implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -7147743627046243795L;
	public String 	_created;
	public String 	_modified;
	public String 	_deleted;
	public Long 	_id;
	private DatabaseAdaptor<?> adaptor = null;
	protected Model() {
		this._created  = ZonedDateTime.now(ZoneOffset.UTC).toString();
		this._modified = ZonedDateTime.now(ZoneOffset.UTC).toString();
		this._deleted = null;
		try {
			adaptor = DatabaseManager.getInstance().createAdaptor(serialVersionUID, BASE_DIR + "resources/json/dbConfig.js");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected Model(List<MinimisedObject> l_mo) {
		this();
		for(MinimisedObject mo : l_mo){
			Class<?> c = this.getClass();
			Field f = null;
			try {
				f = c.getField(mo._name);
				f.set(this, mo._value);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public final T[] insert() throws IllegalArgumentException, IllegalAccessException{
//		DBResult result = adaptor.insert(this.getSerializedModel());
//		return getModel(result);
		return null;
	}
	public final void delete(){
		adaptor.delete(this._id);
	}
	public final T update(String key, Object value){
		return null;
	}
	public final List<T> find(Long id) throws IllegalArgumentException, IllegalAccessException{
		Field f = Arrays.asList(this.getClass().getFields()).stream()
								.filter(field -> field.getName() == "_id")
								.collect(Collectors.toList()).get(0);
		List<List<MinimisedObject>> result = adaptor.find(new MinimisedObject(f, this), this.getClass(), getSerializedModel());
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
	public List<T> getModel(List<List<MinimisedObject>> r) {
		
		List<T> tmp= new ArrayList<>();
		Constructor<?> c;
		for(List<MinimisedObject> r_one : r){
			try {
				c = this.getClass().getConstructor(r_one.getClass());
				@SuppressWarnings("unchecked")
				T o = (T) c.newInstance(r_one);
				tmp.add(o);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tmp;
	}
}
