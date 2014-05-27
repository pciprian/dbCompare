package nbsp.dbcomp.model;

public class InfoColumn {

	private String name;
	private String type;
	private float size;
	private boolean acceptNull;
	private boolean primaryKey;
	
	public InfoColumn(String name, String type, float size, boolean acceptNull, boolean primaryKey) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.acceptNull = acceptNull;
		this.primaryKey = primaryKey;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public boolean isAcceptNull() {
		return acceptNull;
	}
	public void setAcceptNull(boolean acceptNull) {
		this.acceptNull = acceptNull;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	
	
}
