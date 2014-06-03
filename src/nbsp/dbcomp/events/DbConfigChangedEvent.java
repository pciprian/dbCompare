package nbsp.dbcomp.events;

import nbsp.dbcomp.model.enums.DatabaseType;

public class DbConfigChangedEvent implements Event {
	private DatabaseType dbType;
	
	public DbConfigChangedEvent(DatabaseType dbType) {
		this.dbType = dbType;
	}
	
	public DatabaseType getDatabaseType() {
		return dbType;
	}
}
