package nbsp.dbcomp.ui;

import nbsp.dbcomp.bus.EventDispatcher;
import nbsp.dbcomp.bus.EventHandler;
import nbsp.dbcomp.events.ExitEvent;
import nbsp.dbcomp.model.DbConnectionConfigInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
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
	
	public MainDialog() {
		sourceDbConfig = new DbConnectionConfigInfo();
		destinationDbConfig = new DbConnectionConfigInfo();
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
		
		
	}	
}
