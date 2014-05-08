/**
 * 
 */
package nbsp.dbcomp.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import nbsp.dbcomp.model.DbConnectionConfigInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author pciprian
 *
 */
public class DbConnectionConfigDialog extends Dialog {
	private Shell parent;
	private Shell localShell;
	private DbConnectionConfigInfo configInfo;
	private Text inputHost;
	private Text inputPort;
	private Text inputUser;
	private Text inputPass;
	private boolean changed;
	
	public DbConnectionConfigDialog(Shell parent) {
		super(parent);
		this.parent = parent;
	}

	public DbConnectionConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(DbConnectionConfigInfo configInfo) {
		this.configInfo = configInfo;
		if (this.inputHost != null && !this.inputHost.isDisposed()) {
			this.inputHost.setText(this.configInfo.getHost());
		}
		if (this.inputPort != null && !this.inputPort.isDisposed()) {
			this.inputPort.setText(this.configInfo.getPort());
		}
		if (this.inputUser != null && !this.inputUser.isDisposed()) {
			this.inputUser.setText(this.configInfo.getUser());
		}
		if (this.inputPass != null && !this.inputPass.isDisposed()) {
			this.inputPass.setText(this.configInfo.getPass());
		}
	}
	
	public boolean hasChanged() {
		return changed;
	}
	
	public void show() {
		localShell = new Shell(parent, SWT.PRIMARY_MODAL | SWT.DIALOG_TRIM);
		localShell.setSize(400, 300);
		localShell.setText("Database configuration");
		createDialog();
		localShell.open();
 		Display display = localShell.getParent().getDisplay();
		while (!localShell.isDisposed()) {
			if (!display.readAndDispatch()) { 
				display.sleep();
			}
		}		
	}
	
	private void createDialog() {
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		localShell.setLayout(formLayout);
		
		// Buttons
		Button buttonCancel = new Button(localShell, SWT.PUSH | SWT.CENTER);
		buttonCancel.setText("Cancel");
		FormData buttonCancelData = new FormData();
		buttonCancelData.right = new FormAttachment(100, 0);
		buttonCancelData.bottom = new FormAttachment(100, 0);
		buttonCancel.setLayoutData(buttonCancelData);
		
		Button buttonOk = new Button(localShell, SWT.PUSH | SWT.CENTER);
		buttonOk.setText("OK");
		FormData buttonOkData = new FormData();
		buttonOkData.right = new FormAttachment(buttonCancel, -5);
		buttonOkData.bottom = new FormAttachment(100, 0);
		buttonOk.setLayoutData(buttonOkData);
		
		// Configuration group
		Group groupConfig = new Group(localShell, SWT.SHADOW_NONE);
		groupConfig.setLayout(formLayout);
		FormData grpConfigData = new FormData();
		grpConfigData.top = new FormAttachment(0,0);
		grpConfigData.left = new FormAttachment(0,0);
		grpConfigData.right = new FormAttachment(100,0);
		grpConfigData.bottom = new FormAttachment(buttonCancel, -5);
		groupConfig.setLayoutData(grpConfigData);
		
		Label hostLabel = new Label(groupConfig, SWT.NONE);
		hostLabel.setText("Host");
		FormData hostLabelData = new FormData();
		hostLabelData.left = new FormAttachment(0, 0);
		hostLabelData.top = new FormAttachment(0, 0);
		hostLabel.setLayoutData(hostLabelData);
		
		Label portLabel = new Label(groupConfig, SWT.NONE);
		portLabel.setText("Port");
		FormData portLabelData = new FormData();
		portLabelData.left = new FormAttachment(0, 0);
		portLabelData.top = new FormAttachment(hostLabel, 10);
		portLabel.setLayoutData(portLabelData);

		Label userLabel = new Label(groupConfig, SWT.NONE);
		userLabel.setText("Username");
		FormData userLabelData = new FormData();
		userLabelData.left = new FormAttachment(0, 0);
		userLabelData.top = new FormAttachment(portLabel, 10);
		userLabel.setLayoutData(userLabelData);
		
		Label passLabel = new Label(groupConfig, SWT.NONE);
		passLabel.setText("Password");
		FormData passLabelData = new FormData();
		passLabelData.left = new FormAttachment(0, 0);
		passLabelData.top = new FormAttachment(userLabel, 10);
		passLabel.setLayoutData(passLabelData);
		
		inputHost = new Text(groupConfig, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
		inputHost.setText(configInfo.getHost());
		FormData hostInputData = new FormData();
		hostInputData.left = new FormAttachment(40, 0);
		hostInputData.right = new FormAttachment(100, 0);
		hostInputData.top = new FormAttachment(hostLabel, 0, SWT.TOP);
		inputHost.setLayoutData(hostInputData);

		inputPort = new Text(groupConfig, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
		inputPort.setText(configInfo.getPort());
		FormData portInputData = new FormData();
		portInputData.left = new FormAttachment(40, 0);
		portInputData.right = new FormAttachment(100, 0);
		portInputData.top = new FormAttachment(portLabel, 0, SWT.TOP);
		inputPort.setLayoutData(portInputData);

		inputUser = new Text(groupConfig, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
		inputUser.setText(configInfo.getUser());
		FormData userInputData = new FormData();
		userInputData.left = new FormAttachment(40, 0);
		userInputData.right = new FormAttachment(100, 0);
		userInputData.top = new FormAttachment(userLabel, 0, SWT.TOP);
		inputUser.setLayoutData(userInputData);

		inputPass = new Text(groupConfig, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
		inputPass.setText(configInfo.getPass());
		FormData passInputData = new FormData();
		passInputData.left = new FormAttachment(40, 0);
		passInputData.right = new FormAttachment(100, 0);
		passInputData.top = new FormAttachment(passLabel, 0, SWT.TOP);
		inputPass.setLayoutData(passInputData);		
		
		// events listeners
		buttonCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				if (!inputHost.isDisposed()) {
					inputHost.setText(configInfo.getHost());
				}
				if (!inputPort.isDisposed()) {
					inputPort.setText(configInfo.getPort());
				}
				if (!inputUser.isDisposed()) {
					inputUser.setText(configInfo.getUser());
				}
				if (!inputPass.isDisposed()) {
					inputPass.setText(configInfo.getPass());
				}
				if (!localShell.isDisposed()) {
					localShell.close();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing to do here				
			}
			
		});
		
		buttonOk.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!inputHost.isDisposed()) {
					if (!configInfo.getHost().equalsIgnoreCase(inputHost.getText())) {
						changed = true;
					}
					configInfo.setHost(inputHost.getText());
				}
				if (!inputPort.isDisposed()) {
					if (!configInfo.getPort().equalsIgnoreCase(inputPort.getText())) {
						changed = true;
					}
					configInfo.setPort(inputPort.getText());
				}
				if (!inputUser.isDisposed()) {
					if (!configInfo.getUser().equalsIgnoreCase(inputUser.getText())) {
						changed = true;
					}
					configInfo.setUser(inputUser.getText());
				}
				if (!inputPass.isDisposed()) {
					if (!configInfo.getPass().equalsIgnoreCase(inputPass.getText())) {
						changed = true;
					}
					configInfo.setPass(inputPass.getText());
				}
				if (!testDatabaseConnectivity()) {
					// need an error message here
					return;
				}				
				if (!localShell.isDisposed()) {
					localShell.close();
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing to do here				
			}			
		});
	}
	
	private boolean testDatabaseConnectivity() {
		boolean connected = false;
	    try {
			Class.forName(configInfo.getDriverName());
			Connection connection = DriverManager.getConnection(configInfo.getAuthDbConnectionUrl(), 
																configInfo.getUser(), configInfo.getPass());
			connected = true;
			connection.close();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	    return connected;
	}

}
