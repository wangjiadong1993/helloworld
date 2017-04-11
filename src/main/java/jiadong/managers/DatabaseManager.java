package jiadong.managers;

public class DatabaseManager{
	private static DatabaseManager databaseManager;
	private DatabaseManager() {
	}

	public static DatabaseManager getInstance(){
		if(DatabaseManager.databaseManager == null){
			DatabaseManager.databaseManager = new DatabaseManager();
		}
		return DatabaseManager.databaseManager;
	}
}
