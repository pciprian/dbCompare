package nbsp.dbcomp.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfoDbMetadata {

	private List<InfoTable> tables;
	
	public InfoDbMetadata() {
		this.tables = new ArrayList<InfoTable>();
	}

	public List<InfoTable> getTables() {
		return tables;
	}
	
	public void readDbInfo(Connection connection) {
		try {
			DatabaseMetaData dmd = connection.getMetaData();
			ResultSet rsTables = dmd.getTables(null, null, "%", new String[] { "TABLE" } );
			while( rsTables.next() ) {
				String tbName = rsTables.getString( "TABLE_NAME" );
				InfoTable tb = new InfoTable(tbName);
				tables.add( tb );
				readTableInfo(dmd, tb);				
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readTableInfo(DatabaseMetaData dmd, InfoTable tb) {
		try {
			ResultSet rsPK = dmd.getPrimaryKeys(null, null, tb.getName());
			List<String> pkList = new ArrayList<String>();
			while( rsPK.next() ) 
				pkList.add( rsPK.getString( "COLUMN_NAME" ) );
			
			ResultSet rsColumns = dmd.getColumns(null, null, tb.getName(), "%");			
			while( rsColumns.next() ) {
				String colName = rsColumns.getString( "COLUMN_NAME" );
				String colType = rsColumns.getString( "DATA_TYPE" );
				colType += ": ";
				colType += rsColumns.getString( "TYPE_NAME" );
				float colSize = 0;
				if( rsColumns.getObject( "COLUMN_SIZE" ) != null ) {
					colSize += rsColumns.getInt( "COLUMN_SIZE" );
				}
				if( rsColumns.getObject( "DECIMAL_DIGITS" ) != null ) {
					colSize +=  Float.valueOf("0." + rsColumns.getString( "DECIMAL_DIGITS" ));
				}
				boolean colNull = false;
				if( rsColumns.getString("IS_NULLABLE") != null && rsColumns.getString("IS_NULLABLE").compareTo("YES") == 0 )
					colNull = true;
				boolean colPK = false;
				for( String pk : pkList )
					if( pk.compareToIgnoreCase( colName ) == 0 ) 
						colPK = true;
				
				InfoColumn col = new InfoColumn(colName, colType, colSize, colNull, colPK);
				tb.getColumns().add(col);
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
