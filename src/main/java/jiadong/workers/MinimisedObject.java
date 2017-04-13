package jiadong.workers;

import java.lang.reflect.Field;

import jiadong.managers.LoggingManager;

public class MinimisedObject {
	public final String _class;
	public final String _value;
	public final String _name;
	public MinimisedObject(String c, String v, String n){
		_class = c;
		_value = v;
		_name = n;
	}
	public MinimisedObject(Field field, Object obj) throws IllegalArgumentException, IllegalAccessException{
		_class = field.getClass().toString();
		_name = field.getName();
		try{
			LoggingManager.getInstance().log(this, field.get(obj).toString());
		}catch(NullPointerException e){
			LoggingManager.getInstance().log(this, "NULL");
		}
		String val = null;
		try{
			val = field.get(obj).toString();
		}catch(NullPointerException e){
			val = null;
		}finally{
			_value =val;
		}
		
	}
}
