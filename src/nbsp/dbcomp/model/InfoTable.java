package nbsp.dbcomp.model;

import java.util.ArrayList;
import java.util.List;

public class InfoTable {

	private String name;
	private List<InfoColumn> columns;
	private int count;
	
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
