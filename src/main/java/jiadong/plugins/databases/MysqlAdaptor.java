package jiadong.plugins.databases;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	public List<List<MinimisedObject>> find(MinimisedObject object, Class<?>  claz) {
		List<MinimisedObject> al_mo;
		List<List<MinimisedObject>> lt = new ArrayList<>();
		Statement s;
		try {
			s = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String q = "select * from " + claz.getSimpleName() + " where "
				+ object._name.substring(1) + " = " + object._value.toString() + ";";
		LoggingManager.getInstance().log(this, "Query::" + q);
		ResultSet r;
		try {
			r = s.executeQuery(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		try {
			while (r.next()) {
				al_mo = new ArrayList<MinimisedObject>();
				List<Field> al_f = Arrays.asList(claz.getFields()).stream()
						.filter(field -> field.getName().startsWith("_"))
						.collect(Collectors.toList());
				for (Field f : al_f) {
					LoggingManager.getInstance().log(this, "Function To Call : " + "get" + f.getClass().toString());
					Method m = r.getClass().getMethod(
							"get" + f.getClass().toString(), String.class);
					Object o = m.invoke(r, f.getName());
					al_mo.add(new MinimisedObject(f.getClass().toString(), o, f.getName()));
				}
				lt.add(al_mo);
			}
			return lt;
		} catch (SecurityException | NoSuchMethodException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
