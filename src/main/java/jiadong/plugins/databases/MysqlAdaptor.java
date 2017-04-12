package jiadong.plugins.databases;

import java.sql.Connection;
import java.sql.SQLException;

import jiadong.workers.DBResult;
import jiadong.workers.DatabaseAdaptor;

import com.mysql.cj.jdbc.MysqlDataSource;

public class MysqlAdaptor implements DatabaseAdaptor<MysqlAdaptor> {
	public static final String Database_Identifier = "MySQL";
	private Connection connection;
	public MysqlAdaptor(String path){
		
		MysqlDataSource datasource= new MysqlDataSource();
		datasource.setDatabaseName("JDDrive");
		datasource.setUser("root");
		datasource.setPassword("jiadong");
		datasource.setPortNumber(3306);
		datasource.setURL("localhost");
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			connection = null;
		}
	}
	@Override
	public DBResult select(String statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(String statement) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public DBResult update(String statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DBResult insert(String statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void create(String statement) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void alter(String statement) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Connection connect(String url, int port, String name, String passwd,
			String database) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void disconnect(Connection connection) {
		// TODO Auto-generated method stub
		
	}
}
