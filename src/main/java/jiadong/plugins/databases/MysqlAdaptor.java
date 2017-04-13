package jiadong.plugins.databases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jiadong.utils.FileUtil;
import jiadong.utils.JsonUtil;
import jiadong.workers.DBResult;
import jiadong.workers.DatabaseAdaptor;
import jiadong.workers.MinimisedObject;

import com.mysql.cj.jdbc.MysqlDataSource;

public class MysqlAdaptor implements DatabaseAdaptor<MysqlAdaptor> {
	public static final String Database_Identifier = "MySQL";
	private Connection connection;
	public MysqlAdaptor(String path) throws SQLException{
		String config = FileUtil.readFile(path);
		String dbname = JsonUtil.getString(JsonUtil.getJSON(config), "DatabaseName");
		String user = JsonUtil.getString(JsonUtil.getJSON(config), "UserName");
		String pswd = JsonUtil.getString(JsonUtil.getJSON(config), "Password");
		String port = JsonUtil.getString(JsonUtil.getJSON(config), "PortNum");
		String url = JsonUtil.getString(JsonUtil.getJSON(config), "URL");
		MysqlDataSource datasource= new MysqlDataSource();
		datasource.setDatabaseName(dbname);
		datasource.setUser(user);
		datasource.setPassword(pswd);
		datasource.setPortNumber(Integer.parseInt(port));
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	@Override
	public DBResult select(String statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(long statement) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public DBResult update(String statement) {
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
	@Override
	public DBResult insert(ArrayList<MinimisedObject> statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DBResult find(MinimisedObject object, Class<?>  claz) {
		try {
			Statement s = connection.createStatement();
			ResultSet r = s.executeQuery("select * from "+claz.getSimpleName()+" where "+object._name +" = "+object._value +";");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
