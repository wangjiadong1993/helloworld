package jiadong.workers;

import java.sql.Connection;


public interface DatabaseAdaptor {
	public DBResult 	select(String statement);
	public void 	delete(String statement);
	public DBResult 	update(String statement);
	public DBResult 	insert(String statement);
	
	public void 	create(String statement);
	public void 	alter(String statement);
	public Connection 	connect(String url, int port, String name, String passwd, String database);
	public void 	disconnect(Connection connection);
}
