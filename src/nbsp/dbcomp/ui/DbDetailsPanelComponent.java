package nbsp.dbcomp.ui;

import java.util.ArrayList;
import java.util.List;

import nbsp.dbcomp.events.DbConfigChangedEvent.Database;
import nbsp.dbcomp.model.DbConnectionConfigInfo;
import nbsp.dbcomp.model.InfoDbMetadata;
import nbsp.dbcomp.model.InfoTable;

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

public class DbDetailsPanelComponent extends Composite {
	
	private Database database;
	private Group grpMainInfo;
	private Table masterTable;
	private Table slaveTable;
	private InfoDbMetadata metadata;
	private List<Object> handlersList;
	
	public DbDetailsPanelComponent(Composite parent, Database database) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.database = database;
		handlersList = new ArrayList<Object>();
	
		createControls();
	}
	
	private void createControls() {
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		
		grpMainInfo = new Group(this, SWT.SHADOW_NONE);
		grpMainInfo.setText((database == Database.Source)?"Source database":"Destination database");
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
		
		masterTable.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table master = (Table)e.getSource();
				int indexSelected = master.getSelectionIndex();
				if (indexSelected != -1) {
					TableItem row = master.getItem(indexSelected);
					String tableName = row.getText(0);
					// TODO ... call an update event here
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
	            public void handleEvent(Event e) {
	                  onResize();
	            }
	    });				
	}
	
	public Database getDatabase() {
		return database;
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

	public void updateDetails(InfoDbMetadata metadata) {
		this.metadata = metadata;
		updateDetailsTables();
	}
	
	private void updateDetailsTables() {
		if (this.metadata == null) {
			return;
		}
		
		if (!masterTable.isDisposed()) {
			masterTable.clearAll();
			masterTable.setLinesVisible(true);
			masterTable.setHeaderVisible(true);
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
	
	private void loadSlaveTableData(String tableName) {
		// TODO
	}
	
	// TODO - add internal class to help with update
}
