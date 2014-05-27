package nbsp.dbcomp.model;

import java.util.ArrayList;
import java.util.List;

public class InfoTable {

	private String name;
	private List<InfoColumn> columns;
	
	public InfoTable(String name) {
		this.name = name;
		this.columns = new ArrayList<InfoColumn>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<InfoColumn> getColumns() {
		return columns;
	}
		
}
