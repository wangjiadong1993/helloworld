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
			e.printStackTrace();
		}
	}
	public final void insert() {
		try {
			adaptor.insert(this.getSerializedModel(), this.getClass());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public final void delete(){
		try{
			LoggingManager.getInstance().log(this, adaptor.toString());
			adaptor.delete(this._id, this.getClass());
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	public final List<T> update(String key, Object value) throws IllegalArgumentException, IllegalAccessException{
		List<T> l_t = this.find(this._id);
		if(l_t == null){
			return null;
		}else if(l_t.size() == 0){
			return null;
		}else{
			T t = l_t.get(0);
			ArrayList<MinimisedObject> l_mo = t.getSerializedModel();
			List<MinimisedObject> l_mo_new = l_mo.stream().filter(mo -> mo._name == key).collect(Collectors.toList());
			if(l_mo_new == null){
				return null;
			}else if(l_mo_new.size() == 0){
				return null;
			}else{
				l_mo_new.get(0)._value = value;
				List<List<MinimisedObject>> tmp = adaptor.update(this._id, l_mo_new.get(0), this.getClass()); 
				return getModel(tmp);
			}
		}
		
	}
	public final List<T> find(Long id) throws IllegalArgumentException, IllegalAccessException{
		Field f = Arrays.asList(this.getClass().getFields()).stream()
								.filter(field -> field.getName() == "_id")
								.collect(Collectors.toList()).get(0);
		MinimisedObject mo = new MinimisedObject(f, this);
		mo._value = id;
		List<List<MinimisedObject>> result = adaptor.find(mo, this.getClass(), getSerializedModel());
		return getModel(result);
	}
	
	public final List<T> find(String key, Object value) throws IllegalArgumentException, IllegalAccessException{
		Field f = Arrays.asList(this.getClass().getFields()).stream()
								.filter(field -> field.getName() == key)
								.collect(Collectors.toList()).get(0);
		MinimisedObject mo = new MinimisedObject(f, this);
		mo._value = value;
		List<List<MinimisedObject>> result = adaptor.find(mo, this.getClass(), getSerializedModel());
		return getModel(result);
	}	
	
	public ArrayList<MinimisedObject> getSerializedModel() throws IllegalArgumentException, IllegalAccessException{
		List<Field> al_f = Arrays.asList(this.getClass().getFields()).stream()
										.filter(field -> field.getName().startsWith("_"))
										.collect(Collectors.toList());
		ArrayList<MinimisedObject> al = new ArrayList<>();
		for(Field field : al_f){
			al.add(new MinimisedObject(field, this));
		}
		return al;
	}  
	public List<T> getModel(List<List<MinimisedObject>> r) {
		if(r == null ) return null;
		List<T> tmp= new ArrayList<>();
		Constructor<?> con;
		for(List<MinimisedObject> l_mo : r){
			try {
				con = this.getClass().getConstructor();
				@SuppressWarnings("unchecked")
				T o = (T) con.newInstance();
				for(MinimisedObject mo : l_mo){
					Class<?> c = o.getClass();
					Field f = null;
					try {
						f = c.getField("_"+mo._name);
						f.set(o, mo._value);
					} catch (NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				tmp.add(o);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return tmp;
	}
	@Override
	public String toString(){
		String tmp = " ";
		List<MinimisedObject> l_mo;
		try {
			l_mo = getSerializedModel();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} 
		for(MinimisedObject mo : l_mo){
			tmp += mo._name;
			tmp += "::";
			try{
				tmp += mo._value.toString();
			}catch(NullPointerException e){
				tmp += "NULL";
			}
		}
		tmp += " ";
		return tmp;
	}
}
