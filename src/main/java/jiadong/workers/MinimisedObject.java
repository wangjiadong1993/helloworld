package jiadong.workers;

import java.lang.reflect.Field;
public class MinimisedObject {
	public  String _class;
	public  Object _value;
	public  String _name;
	public MinimisedObject(String c, Object v, String n){
		_class = c;
		_value = v;
		_name = n;
	}
	public MinimisedObject(Field field, Object obj) throws IllegalArgumentException, IllegalAccessException{
		_class = field.getType().getSimpleName();
		_name = field.getName();

		Object val = null;
		try{
			val = field.get(obj);
		}catch(NullPointerException e){
			val = null;
		}finally{
			_value =val;
		}
	}
}
