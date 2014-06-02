package nbsp.dbcomp.events;

import nbsp.dbcomp.events.DbConfigChangedEvent.Database;

public class SlaveTableUpdateEvent implements Event {

	private String tableName;
	private Database database;
	
	public SlaveTableUpdateEvent(String tableName, Database database) {
		this.tableName = tableName;
		this.database = database;
	}

	public String getTableName() {
		return tableName;
	}

	public Database getDatabase() {
		return database;
	}
}
