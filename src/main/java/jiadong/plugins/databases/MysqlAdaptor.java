package jiadong.plugins.databases;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jiadong.managers.LoggingManager;
import jiadong.utils.FileUtil;
import jiadong.utils.JsonUtil;
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
	public List<List<MinimisedObject>> select(String statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(long statement) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<List<MinimisedObject>> update(String statement) {
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
	public List<List<MinimisedObject>> insert(ArrayList<MinimisedObject> statement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<List<MinimisedObject>> find(MinimisedObject object, Class<?>  claz, List<MinimisedObject> fields) {
		ResultSet r = null;
		List<List<MinimisedObject>> l_l_mo = new ArrayList<>();
		try {
			Statement s = connection.createStatement();
			String q = "select * from "+claz.getSimpleName()+" where "+object._name.substring(1) +" = "+object._value +";";
			LoggingManager.getInstance().log(this, "Query::"+ q);
			r = s.executeQuery(q);
		} catch (SQLException e) {
			e.printStackTrace();
			r = null;
		}
		if(r == null){return null;}
		try {
			List<MinimisedObject> l_mo;
			while(r.next()){
				l_mo = new ArrayList<>();
				for(MinimisedObject mo : fields){
					String type = mo._class;
					String name = mo._name.substring(1);
					Method m = r.getClass().getMethod("get"+type, String.class);
					mo._value = m.invoke(r, name);
					l_mo.add(new MinimisedObject(type, mo._value, name));
				}
				l_l_mo.add(l_mo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
		} catch (NoSuchMethodException e){
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return l_l_mo;
	}
}
