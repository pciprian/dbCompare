package nbsp.dbcomp.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nbsp.dbcomp.bus.EventDispatcher;
import nbsp.dbcomp.bus.EventHandler;
import nbsp.dbcomp.events.DbConfigChangedEvent;
import nbsp.dbcomp.events.DbConfigChangedEvent.Database;
import nbsp.dbcomp.events.DbSwitchEvent;
import nbsp.dbcomp.events.ExitEvent;
import nbsp.dbcomp.model.DbConnectionConfigInfo;
import nbsp.dbcomp.model.InfoDbMetadata;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author pciprian
 *
 */
public class MainDialog {
	private Shell shell;
	private DbConnectionConfigInfo sourceDbConfig;
	private DbConnectionConfigInfo destinationDbConfig;
	private List<Object> handlersList;
	private int selectedDatabase; // 0 - authentication, 1 - characters
	
	public MainDialog() {
		sourceDbConfig = new DbConnectionConfigInfo();
		destinationDbConfig = new DbConnectionConfigInfo();
		handlersList = new ArrayList<Object>();
		selectedDatabase = 0;
	}
	
	@EventHandler
	public void handleExit(ExitEvent event) {
		if (shell != null) {
			shell.close();
		}
	}
	
	/**
	 * Creates the SWT display, creates and run the main dialog 
	 */
	public void showDialog() {
	    Display display = new Display();
	    shell = new Shell(display);
	    // Layout
	    createForm();
	    // show
		shell.pack();
		shell.setSize(950, 550);		
		shell.setMinimumSize(400, 300);
		shell.setText("Trinity database comparer");
	    shell.open();
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch())
	            display.sleep();
	    }
	    display.dispose();			
	}
	
	private void createForm() {		
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		shell.setLayout(formLayout);
		
		createMainBar(shell);
		createDetailPanels(shell);
	}
	
	private void createMainBar(Composite parent) {
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;		
		
		Group grpMainBar = new Group(parent, SWT.SHADOW_NONE);
		grpMainBar.setLayout(formLayout);
		FormData grpMainBarData = new FormData();
		grpMainBarData.height = 60;
		grpMainBarData.top = new FormAttachment(0,-5);
		grpMainBarData.left = new FormAttachment(0,0);
		grpMainBarData.right = new FormAttachment(100,0);
		grpMainBar.setLayoutData(grpMainBarData);
		
		Button btnExit = new Button(grpMainBar, SWT.PUSH);
		btnExit.setText("Exit");
		FormData btnExitData = new FormData();
		btnExitData.right = new FormAttachment(100,0);
		btnExitData.top = new FormAttachment(0,0);
		btnExit.setLayoutData(btnExitData);
		btnExit.addSelectionListener( new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				EventDispatcher.getInstance().publish(new ExitEvent());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing				
			}			
		});
		
		Label sourceDbLabel = new Label(grpMainBar, SWT.LEFT);
		sourceDbLabel.setText("Source database");
		FormData sourceDbLabelData = new FormData();
		sourceDbLabelData.left = new FormAttachment(0, 0);
		sourceDbLabelData.top = new FormAttachment(0, 0);
		sourceDbLabel.setLayoutData(sourceDbLabelData);
		
		Label destinationDbLabel = new Label(grpMainBar, SWT.LEFT);
		destinationDbLabel.setText("Destination database");
		FormData destinationDbLabelData = new FormData();
		destinationDbLabelData.left = new FormAttachment(0, 0);
		destinationDbLabelData.top = new FormAttachment(sourceDbLabel, 10);
		destinationDbLabel.setLayoutData(destinationDbLabelData);
		
		Button sourceDbConfigButton = new Button(grpMainBar, SWT.PUSH);
		sourceDbConfigButton.setText("Config");
		FormData sourceDbConfigButtonData = new FormData();
		sourceDbConfigButtonData.left = new FormAttachment(0, 140);
		sourceDbConfigButtonData.top = new FormAttachment(0, -5);
		sourceDbConfigButton.setLayoutData(sourceDbConfigButtonData);
		
		Button destinationDbConfigButton = new Button(grpMainBar, SWT.PUSH);
		destinationDbConfigButton.setText("Config");
		FormData destinationDbConfigButtonData = new FormData();
		destinationDbConfigButtonData.left = new FormAttachment(0, 140);
		destinationDbConfigButtonData.top = new FormAttachment(sourceDbConfigButton, 0);
		destinationDbConfigButton.setLayoutData(destinationDbConfigButtonData);
		
		Label sourceDbConnectionDesc = new Label(grpMainBar, SWT.LEFT);
		sourceDbConnectionDesc.setText("No connection info");
		FormData sourceDbConnectionDescData = new FormData();
		sourceDbConnectionDescData.left = new FormAttachment(0, 200);
		sourceDbConnectionDescData.top = new FormAttachment(0, 0);
		sourceDbConnectionDesc.setLayoutData(sourceDbConnectionDescData);
		DbDescriptionLabelHandler sourceDescLabelHandler = new DbDescriptionLabelHandler(sourceDbConnectionDesc, Database.Source);
		EventDispatcher.getInstance().registerHandlers(sourceDescLabelHandler);
		handlersList.add(sourceDescLabelHandler);
		
		Label destinationDbConnectionDesc = new Label(grpMainBar, SWT.LEFT);
		destinationDbConnectionDesc.setText("No connection info");
		FormData destinationDbConnectionDescData = new FormData();
		destinationDbConnectionDescData.left = new FormAttachment(0, 200);
		destinationDbConnectionDescData.top = new FormAttachment(sourceDbConnectionDesc, 10);
		destinationDbConnectionDesc.setLayoutData(destinationDbConnectionDescData);
		DbDescriptionLabelHandler destDescLabelHandler = new DbDescriptionLabelHandler(destinationDbConnectionDesc, Database.Destination);
		EventDispatcher.getInstance().registerHandlers(destDescLabelHandler);
		handlersList.add(destDescLabelHandler);
		
		Combo cboDatabase = new Combo(grpMainBar, SWT.DROP_DOWN|SWT.READ_ONLY);
		cboDatabase.add("Authentication DB", 0);
		cboDatabase.add("Characters DB", 1);
		cboDatabase.select(selectedDatabase);
		FormData cboDatabaseData = new FormData();
		cboDatabaseData.right = new FormAttachment(btnExit, -10);
		cboDatabaseData.top = new FormAttachment(0, 0);
		cboDatabase.setLayoutData(cboDatabaseData);
		
		Label labelCboDatabase = new Label(grpMainBar, SWT.LEFT);
		labelCboDatabase.setText("Select a database");
		FormData labelCboDatabaseData = new FormData();
		labelCboDatabaseData.right = new FormAttachment(cboDatabase, -10);
		labelCboDatabaseData.top = new FormAttachment(0, 5);
		labelCboDatabase.setLayoutData(labelCboDatabaseData);
		
		sourceDbConfigButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DbConnectionConfigDialog sDialog = new DbConnectionConfigDialog(shell);
				sDialog.setConfigInfo(sourceDbConfig);
				sDialog.show();
				if (sDialog.hasChanged()) {
					sourceDbConfig = sDialog.getConfigInfo();
					EventDispatcher.getInstance().publish(new DbConfigChangedEvent(Database.Source));
				}
			}			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing				
			}
		});
		
		destinationDbConfigButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DbConnectionConfigDialog dDialog = new DbConnectionConfigDialog(shell);
				dDialog.setConfigInfo(destinationDbConfig);
				dDialog.show();
				if (dDialog.hasChanged()) {
					destinationDbConfig = dDialog.getConfigInfo();
					EventDispatcher.getInstance().publish(new DbConfigChangedEvent(Database.Destination));
				}
			}			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing				
			}
		});
		
		cboDatabase.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = ((Combo)e.getSource()).getSelectionIndex();
				if (selection != selectedDatabase) {
					selectedDatabase = selection;
					EventDispatcher.getInstance().publish(new DbSwitchEvent());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
				// do nothing
			}
		});
	}
	
	private void createDetailPanels(Composite parent) {
		
		DbDetailsPanelComponent sourcePanel = new DbDetailsPanelComponent(parent, Database.Source);
		FormData sourcePanelData = new FormData();
		sourcePanelData.top = new FormAttachment(0, 80);
		sourcePanelData.left = new FormAttachment(0, 0);
		sourcePanelData.right = new FormAttachment(50, -5);
		sourcePanelData.bottom = new FormAttachment(100,0);
		sourcePanel.setLayoutData(sourcePanelData);
		DbDetailsPanelHandler sourcePanelHandler = new DbDetailsPanelHandler(sourcePanel);
		EventDispatcher.getInstance().registerHandlers(sourcePanelHandler);
		handlersList.add(sourcePanelHandler);
		
		DbDetailsPanelComponent destinationPanel = new DbDetailsPanelComponent(parent, Database.Destination);
		FormData destinationPanelData = new FormData();
		destinationPanelData.top = new FormAttachment(0, 80);
		destinationPanelData.left = new FormAttachment(50, 5);
		destinationPanelData.right = new FormAttachment(100,0);
		destinationPanelData.bottom = new FormAttachment(100,0);
		destinationPanel.setLayoutData(destinationPanelData);
		DbDetailsPanelHandler destPanelHandler = new DbDetailsPanelHandler(destinationPanel);
		EventDispatcher.getInstance().registerHandlers(destPanelHandler);
		handlersList.add(destPanelHandler);
	}
	
	public class DbDescriptionLabelHandler {
		
		private String DB_CONNECT_INFO_FORMAT = "Connection to host %s:%s - user: %s"; 
		private Label labelToUpdate;	
		private Database database;
		
		public DbDescriptionLabelHandler(Label labelToUpdate, Database database) {
			this.labelToUpdate = labelToUpdate;
			this.database = database;
		}
		
		@EventHandler
		public void handleLabelUpdate(DbConfigChangedEvent event) {
			if (!labelToUpdate.isDisposed() && database == event.getDatabaseType()) {
				DbConnectionConfigInfo dbInfo = (event.getDatabaseType() == Database.Source)? sourceDbConfig : destinationDbConfig;
				labelToUpdate.setText(String.format(DB_CONNECT_INFO_FORMAT, dbInfo.getHost(), dbInfo.getPort(), dbInfo.getUser()));
				labelToUpdate.redraw();
				labelToUpdate.update();
				labelToUpdate.getParent().layout();
			}
		}
	}
	
	public class DbDetailsPanelHandler {
		
		private DbDetailsPanelComponent detailsPanel;
		
		public DbDetailsPanelHandler(DbDetailsPanelComponent detailsPanel) {
			this.detailsPanel = detailsPanel;
		}
		
		@EventHandler
		public void handlePanelUpdate(DbConfigChangedEvent event) {
			if (!detailsPanel.isDisposed() && detailsPanel.getDatabase() == event.getDatabaseType()) {
				readInfoAndUpdate();
			}
		}
		
		@EventHandler
		public void handleDatabaseChange(DbSwitchEvent event) {
			readInfoAndUpdate();
		}
		
		private void readInfoAndUpdate() {
			DbConnectionConfigInfo dbInfo = (detailsPanel.getDatabase() == Database.Source)? sourceDbConfig : destinationDbConfig;
			if (dbInfo.isValidConnection()) {
			    try {
					Class.forName(dbInfo.getDriverName());
					String connectionUrl = selectedDatabase == 0? dbInfo.getAuthDbConnectionUrl() : dbInfo.getCharactersDbConnectionUrl();
					Connection connection = DriverManager.getConnection(connectionUrl, dbInfo.getUser(), dbInfo.getPass());
					InfoDbMetadata metadata = new InfoDbMetadata();
					metadata.readDbInfo(connection);
					connection.close();
					detailsPanel.updateDetails(metadata);
				} catch (ClassNotFoundException e) {
					// TODO
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO 
					e.printStackTrace();
				}					
			}			
		}
	}
}
