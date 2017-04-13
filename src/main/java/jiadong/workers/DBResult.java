package jiadong.workers;

public interface DBResult {
	boolean next() throws Exception;
	String getString(int columnIndex) throws Exception;
	boolean getBoolean(int columnIndex) throws Exception;
	int getInt(int columnIndex) throws Exception;
	long getLong(int columnIndex) throws Exception;
	float getFloat(int columnIndex) throws Exception;
}
