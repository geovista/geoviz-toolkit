/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Frank Hardisty and Linna Li */

package geovista.collaboration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSpatialExtentListener extends Remote {
	public void extentChanged(String source, double[] extent)
			throws RemoteException;
}
