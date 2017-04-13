package jiadong.workers;

import java.sql.Connection;
import java.util.ArrayList;


public interface DatabaseAdaptor<T extends DatabaseAdaptor<T>>{
	public static final String Database_Identifier = "Dummy";
	public DBResult 	select(String statement);
	public void 		delete(long id);
	public DBResult 	update(String statement);
	public DBResult 	insert(ArrayList<MinimisedObject> statement);
	public DBResult 	find(MinimisedObject object, Class<?> claz);
	
	
	public void 		create(String statement);
	public void 		alter(String statement);
	public Connection 	connect(String url, int port, String name, String passwd, String database);
	public void 		disconnect(Connection connection);
	public default 		String getIdentifier(){
		return T.Database_Identifier;
	}
}