/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSelectionListener extends Remote {
	public void selectionChanged(String source, int[] selection)
			throws RemoteException;
}
