/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSubspaceListener extends Remote {
	public void subspaceChanged(String source, int[] subspace)
			throws RemoteException;
}
