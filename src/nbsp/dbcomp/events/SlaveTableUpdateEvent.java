package nbsp.dbcomp.events;

import nbsp.dbcomp.model.enums.DatabaseType;


public class SlaveTableUpdateEvent implements Event {

	private String tableName;
	private DatabaseType database;
	
	public SlaveTableUpdateEvent(String tableName, DatabaseType database) {
		this.tableName = tableName;
		this.database = database;
	}

	public String getTableName() {
		return tableName;
	}

	public DatabaseType getDatabase() {
		return database;
	}
}
