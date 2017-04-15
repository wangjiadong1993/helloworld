package jiadong.workers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public interface DatabaseAdaptor<T extends DatabaseAdaptor<T>>{
	public static final String Database_Identifier = "Dummy";
	public List<List<MinimisedObject>> 	select(String statement);
	public void 						delete(Long id, Class<?> claz);
	public List<List<MinimisedObject>> 	update(Long id,MinimisedObject attribute, Class<?> claz);
	public List<List<MinimisedObject>> 	insert(ArrayList<MinimisedObject> statement, Class<?> claz);
	public List<List<MinimisedObject>> 	find(MinimisedObject object, Class<?> claz, List<MinimisedObject> fields);
	
	public void 		create(String statement);
	public void 		alter(String statement);
	public Connection 	connect(String url, int port, String name, String passwd, String database);
	public void 		disconnect(Connection connection);
	public default 		String getIdentifier(){
		return T.Database_Identifier;
	}
}