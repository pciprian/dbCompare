package nbsp.dbcomp.events;

public class DbConfigChangedEvent implements Event {
	public enum Database {
		Source,
		Destination;
	}
	
	private Database dbType;
	
	public DbConfigChangedEvent(Database dbType) {
		this.dbType = dbType;
	}
	
	public Database getDatabaseType() {
		return dbType;
	}
}
