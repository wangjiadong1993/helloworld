package jiadong.plugins.portListeners.http.mvc;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

public abstract class Model<T extends Model<T>> implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = -7147743627046243795L;
	public String 	_created;
	public String 	_modified;
	public String 	_deleted;
	public 	Long 	_id;
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
	Model(List<MinimisedObject> al_o){
		this();
		List<Field> al_f = Arrays.asList(this.getClass().getFields()).stream()
				.filter(field -> field.getName().startsWith("_"))
				.collect(Collectors.toList());
		for(Field f : al_f){
			MinimisedObject mo = al_o.stream().filter(o -> "_" + o._name == f.getName()).findFirst().get();
			try {
				f.set(this, mo._value);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public final List<T> insert(){
//		DBResult result = adaptor.insert(this.getSerializedModel());
//		try {
//			return getModel(result);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
		return null;
	}
	public final void delete(){
		adaptor.delete(this._id);
	}
	public final T update(String key, Object value){
		return null;
	}
	public final List<T> find(Long id) throws Exception{
		Field f = Arrays.asList(this.getClass().getFields()).stream()
								.filter(field -> field.getName() == "_id")
								.collect(Collectors.toList()).get(0);
		MinimisedObject mo = new MinimisedObject(f, this);
		mo._value = id;
		List<List<MinimisedObject>> result = adaptor.find(mo, this.getClass());
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
	public List<T> getModel(List<List<MinimisedObject>> r) throws Exception {
		List<T> lt = new ArrayList<T>();
		for(List<MinimisedObject> l_mo : r){
			Class<?> c  = this.getClass();
			Constructor<?> con = c.getConstructor(l_mo.getClass());
			@SuppressWarnings("unchecked")
			T o = (T) con.newInstance(l_mo);
			lt.add(o);
		}
		return lt;
	}
}
