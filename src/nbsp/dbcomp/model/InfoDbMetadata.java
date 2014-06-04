package nbsp.dbcomp.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nbsp.dbcomp.model.enums.DatabaseSelection;

import com.mysql.jdbc.PreparedStatement;

public class InfoDbMetadata {

	private DbConnectionConfigInfo connectionInfo;
	private DatabaseSelection selectedDatabase;
	private List<InfoTable> tables;	
	
	public InfoDbMetadata(DbConnectionConfigInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
		this.tables = new ArrayList<InfoTable>();
	}

	public List<InfoTable> getTables() {
		return tables;
	}
	
	public DbConnectionConfigInfo getConnectionInfo() {
		return connectionInfo;
	}
	
	public DatabaseSelection getSelectedDatabase() {
		return selectedDatabase;
	}

	public void setSelectedDatabase(DatabaseSelection selectedDatabase) {
		this.selectedDatabase = selectedDatabase;
	}

	public void readDbInfo() {
		if (!connectionInfo.isValidConnection()) {
			return;
		}
		if (selectedDatabase == null) {
			return;
		}
		
		try {
			Class.forName(connectionInfo.getDriverName());
			String connectionUrl = connectionInfo.getDatabaseConnectionUrl(selectedDatabase);
			Connection connection = DriverManager.getConnection(connectionUrl, connectionInfo.getUser(), connectionInfo.getPass());			
			DatabaseMetaData dmd = connection.getMetaData();
			ResultSet rsTables = dmd.getTables(null, null, "%", new String[] { "TABLE" } );
			while( rsTables.next() ) {
				String tbName = rsTables.getString( "TABLE_NAME" );
				InfoTable tb = new InfoTable(tbName);
				tables.add( tb );
				readTableInfo(dmd, tb);
				PreparedStatement ps = (PreparedStatement) connection.prepareStatement("select count(*) from "+tbName);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					int rowCount = rs.getInt(1);
					tb.setCount(rowCount);
				}
				rs.close();
				ps.close();
			}	
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
