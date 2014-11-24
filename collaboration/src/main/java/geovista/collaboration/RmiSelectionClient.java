/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Frank Hardisty and Linna Li */
package geovista.collaboration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class RmiSelectionClient {
	protected final static Logger logger = Logger
			.getLogger(RmiSelectionClient.class.getName());

	static public void main(String args[]) {
		ReceiveMessage rmiListener;
		Registry registry;
		String listenerAddress = args[0];
		String listenerPort = args[1];
		String text = args[2];
		logger.finest("sending " + text + " to " + listenerAddress + ":"
				+ listenerPort);
		try {
			// get the “registry"
			registry = LocateRegistry.getRegistry(listenerAddress,
					(new Integer(listenerPort)).intValue());
			// look up the remote object
			rmiListener = (ReceiveMessage) (registry.lookup("rmiListener"));

			// call the remote method
			// rmiListener.receiveMessage(text);
			RemoteSelectionListener selectionListener = (RemoteSelectionListener) rmiListener;
			int[] sel = { 1, 2, 3 };
			selectionListener.selectionChanged("Frank", sel);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
