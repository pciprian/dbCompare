package nbsp.dbcomp.ui;

import nbsp.dbcomp.events.DbConfigChangedEvent.Database;
import nbsp.dbcomp.model.DbConnectionConfigInfo;
import nbsp.dbcomp.model.InfoDbMetadata;

import org.eclipse.swt.SWT;
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

public class DbDetailsPanelComponent extends Composite {
	
	private Database database;
	private Group grpMainInfo;
	private Table masterTable;
	private Table slaveTable;
	private InfoDbMetadata metadata;
	
	public DbDetailsPanelComponent(Composite parent, Database database) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.database = database;
	
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
		
		masterTable = new Table(grpMainInfo, SWT.SINGLE | SWT.BORDER);
		FormData masterTableData = new FormData();
		masterTableData.top = new FormAttachment(0,0);
		masterTableData.left = new FormAttachment(0,0);
		masterTableData.right = new FormAttachment(100,0);
		masterTableData.bottom = new FormAttachment(60,-5);
		masterTable.setLayoutData(masterTableData);
		
		slaveTable = new Table(grpMainInfo, SWT.SINGLE | SWT.BORDER);
		FormData slaveTableData = new FormData();
		slaveTableData.top = new FormAttachment(60,5);
		slaveTableData.left = new FormAttachment(0,0);
		slaveTableData.right = new FormAttachment(100,0);
		slaveTableData.bottom = new FormAttachment(100,0);
		slaveTable.setLayoutData(slaveTableData);
		
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
	}	

	public void updateDetails(InfoDbMetadata metadata) {
		this.metadata = metadata;
		updateDetailsTables();
	}
	
	private void updateDetailsTables() {
		
	}
		
}
