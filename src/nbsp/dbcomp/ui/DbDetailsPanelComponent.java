package nbsp.dbcomp.ui;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nbsp.dbcomp.bus.EventDispatcher;
import nbsp.dbcomp.bus.EventHandler;
import nbsp.dbcomp.events.SlaveTableUpdateEvent;
import nbsp.dbcomp.model.DbConnectionConfigInfo;
import nbsp.dbcomp.model.InfoColumn;
import nbsp.dbcomp.model.InfoDbMetadata;
import nbsp.dbcomp.model.InfoTable;
import nbsp.dbcomp.model.enums.DatabaseSelection;
import nbsp.dbcomp.model.enums.DatabaseType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.mysql.jdbc.PreparedStatement;

public class DbDetailsPanelComponent extends Composite {
	
	private DatabaseType databaseType;
	private Group grpMainInfo;
	private Table masterTable;
	private Table slaveTable;
	private InfoDbMetadata metadata;
	private List<Object> handlersList;
	
	public DbDetailsPanelComponent(Composite parent, DatabaseType databaseType) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.databaseType = databaseType;
		handlersList = new ArrayList<Object>();
	
		createControls();
	}
	
	private void createControls() {
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		
		grpMainInfo = new Group(this, SWT.SHADOW_NONE);
		grpMainInfo.setText((databaseType == DatabaseType.Source)?"Source database":"Destination database");
		setBackground(grpMainInfo.getBackground());
		setForeground(grpMainInfo.getForeground());
		
		grpMainInfo.setLayout(formLayout);
		FormData grpMainInfoData = new FormData();
		grpMainInfoData.top = new FormAttachment(0,0);
		grpMainInfoData.left = new FormAttachment(0,0);
		grpMainInfoData.right = new FormAttachment(100,0);
		grpMainInfoData.bottom = new FormAttachment(100,0);
		grpMainInfo.setLayoutData(grpMainInfoData);
		
		masterTable = new Table(grpMainInfo, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		FormData masterTableData = new FormData();
		masterTableData.top = new FormAttachment(0,0);
		masterTableData.left = new FormAttachment(0,0);
		masterTableData.right = new FormAttachment(100,0);
		masterTableData.bottom = new FormAttachment(60,-5);
		masterTable.setLayoutData(masterTableData);
		
		slaveTable = new Table(grpMainInfo, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		FormData slaveTableData = new FormData();
		slaveTableData.top = new FormAttachment(60,5);
		slaveTableData.left = new FormAttachment(0,0);
		slaveTableData.right = new FormAttachment(100,0);
		slaveTableData.bottom = new FormAttachment(100,0);
		slaveTable.setLayoutData(slaveTableData);
		
		SlaveTableUpdateHandle panelHandle = new SlaveTableUpdateHandle(this);
		this.handlersList.add(panelHandle);
		EventDispatcher.getInstance().registerHandlers(panelHandle);
		
		masterTable.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table master = (Table)e.getSource();
				int indexSelected = master.getSelectionIndex();
				if (indexSelected != -1) {
					TableItem row = master.getItem(indexSelected);
					String tableName = row.getText(0);
					SlaveTableUpdateEvent event = new SlaveTableUpdateEvent(tableName, databaseType);
					EventDispatcher.getInstance().publish(event);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing here				
			}
		});
		
		if (metadata != null) {
			updateDetailsTables();
		}
		
	    addListener(SWT.Resize, new Listener() {
	    	@Override
            public void handleEvent(Event e) {
            	onResize();
	        }
	    });
	    
	    addListener(SWT.Dispose, new Listener() {			
			@Override
			public void handleEvent(Event event) {
				onDispose();
			}
		});
	}
	
	public DatabaseType getDatabaseType() {
		return databaseType;
	}
	
	public InfoDbMetadata getMetadata() {
		return metadata;
	}
	
	public Table getMasterTable() {
		return masterTable;
	}
	
	public Table getSlaveTable() {
		return slaveTable;
	}

	private void onResize () {
	    Rectangle area = getClientArea();
	    grpMainInfo.setBounds(0, 0, area.width, area.height);
	    if (masterTable.getColumnCount() > 0) {
	    	int availableWidth = masterTable.getClientArea().width;
	    	int firstColumnWidth = availableWidth * 2 / 3;
	    	int secondColumnWidth = availableWidth - firstColumnWidth - 1;
	    	masterTable.getColumn(0).setWidth(firstColumnWidth);
	    	masterTable.getColumn(1).setWidth(secondColumnWidth);
	    }
	}
	
	private void onDispose() {
		
	}

	public void updateDetails(InfoDbMetadata metadata) {
		this.metadata = metadata;
		updateDetailsTables();
	}
	
	private void updateDetailsTables() {
		if (this.metadata == null) {
			return;
		}
		
		if (!masterTable.isDisposed()) {
			masterTable.removeAll();
			masterTable.setLinesVisible(true);
			masterTable.setHeaderVisible(true);
			while ( masterTable.getColumnCount() > 0 ) {
				masterTable.getColumns()[ 0 ].dispose();
			}
			
	    	int availableWidth = masterTable.getClientArea().width;
	    	int firstColumnWidth = availableWidth * 2 / 3;
	    	int secondColumnWidth = availableWidth - firstColumnWidth - 1;
			TableColumn tc1 = new TableColumn(masterTable, SWT.LEFT);
			tc1.setText("Name");
			tc1.setWidth(firstColumnWidth);
			TableColumn tc2 = new TableColumn(masterTable, SWT.RIGHT);
			tc2.setText("Count");
			tc2.setWidth(secondColumnWidth);
			loadMasterTableData();
			masterTable.redraw();
			masterTable.update();
			if (!this.isDisposed()) {
				this.layout();
			}
		}
	}
	
	private void loadMasterTableData() {
		if (this.metadata == null) {
			return;
		}
		if (!masterTable.isDisposed()) {
			for (InfoTable table : metadata.getTables()) {
				TableItem item = new TableItem(masterTable, SWT.NONE);
				item.setText(0, table.getName());
				item.setText(1, ""+table.getCount());
			}
		}
	}
	
	public class SlaveTableUpdateHandle {
		
		private DbDetailsPanelComponent panel;
		
		public SlaveTableUpdateHandle(DbDetailsPanelComponent panel) {
			this.panel = panel;
		}
		
		@EventHandler
		public void handleSlaveUpdate(SlaveTableUpdateEvent event) {
			if (event.getDatabase() == this.panel.getDatabaseType()) {
				Table localTable = this.panel.getSlaveTable();
				localTable.removeAll();
				localTable.setLinesVisible(true);
				localTable.setHeaderVisible(true);
				while ( localTable.getColumnCount() > 0 ) {
					localTable.getColumns()[ 0 ].dispose();
				}				
				
				InfoDbMetadata localMetadata = this.panel.getMetadata();
				InfoTable localInfoTable = null;
				for (InfoTable it : localMetadata.getTables()) {
					if (it.getName().equalsIgnoreCase(event.getTableName())) {
						localInfoTable = it;
					}
				}
				if (localInfoTable == null) {
					return;
				}
				int availableWidth = localTable.getClientArea().width;
				int columnWidth = availableWidth / localInfoTable.getColumns().size();
				if (columnWidth < 100) {
					columnWidth = 100;
				}
				for (InfoColumn ic : localInfoTable.getColumns()) {
					TableColumn column = new TableColumn(localTable, SWT.LEFT);
					column.setText(ic.getName());
					column.setWidth(columnWidth);
				}
				
				String connectionUrl = localMetadata.getConnectionInfo().getDatabaseConnectionUrl(localMetadata.getSelectedDatabase());
				try {
					Class.forName(localMetadata.getConnectionInfo().getDriverName());
					
					Connection connection = DriverManager.getConnection(connectionUrl,
																		localMetadata.getConnectionInfo().getUser(),
																		localMetadata.getConnectionInfo().getPass());

					PreparedStatement ps = (PreparedStatement) connection.prepareStatement("select * from "+event.getTableName());
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						TableItem item = new TableItem(localTable, SWT.NONE);
						int position = 0;
						for (TableColumn tc : localTable.getColumns()) {
							String value = rs.getString(tc.getText());
							if (value == null) {
								value = "";
							}
							item.setText(position, value);
							position++;
						}						
					}
					rs.close();
					ps.close();
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}
}
