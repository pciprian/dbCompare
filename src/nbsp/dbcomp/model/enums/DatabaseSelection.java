package nbsp.dbcomp.model.enums;

public enum DatabaseSelection {
	Authentication(0),
	Characters(1);
	
	private int indexValue;
	
	private DatabaseSelection(int index) {
		this.indexValue = index;
	}
	
	public int indexValue() {
		return indexValue;
	}
	
	public static DatabaseSelection fromIndexValue(int index) {
		for(DatabaseSelection dbSel : DatabaseSelection.values()) {
			if (dbSel.indexValue() == index) {
				return dbSel;
			}
		}
		return null;
	}
}