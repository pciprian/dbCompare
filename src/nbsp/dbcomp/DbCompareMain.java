/**
 * 
 */
package nbsp.dbcomp;

import nbsp.dbcomp.bus.EventDispatcher;
import nbsp.dbcomp.ui.MainDialog;

/**
 * @author pciprian
 *
 */
public class DbCompareMain {

	/**
	 * Entry point - instantiate and runs the main dialog.
	 * @param args not used
	 */
	public static void main(String[] args) {
		MainDialog dialog = new MainDialog();
		EventDispatcher.getInstance().registerHandlers(dialog);
		dialog.showDialog();
	}
	


	

}
