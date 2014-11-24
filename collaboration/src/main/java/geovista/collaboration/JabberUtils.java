/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.DefaultPacketExtension;

public abstract class JabberUtils {
	public static final String SELECTION_ELEMENT_NAME = "Selection";
	public static final String SUBSPACE_ELEMENT_NAME = "Subspace";
	public static final String SPATIAL_EXTENT_ELEMENT_NAME = "SpatialExtent";
	public static final String MESSAGE_ELEMENT_NAME = "MessageElement";
	public static final String MARSHALED_COMPONENT_ELEMENT_NAME = "MarshaledComponent";
	public static final String DEFAULT_NAMESPACE = "geoviz";
	public static final String LENGTH_STRING = "length";
	public static final String MARSHALLED_COMPONENT_XML_STRING = "ComponentXML";
	public static final String MESSAGE_BODY_STRING = "MessageBody";
	public static final int STATE_LEADER = 2;// don't apply packets
	public static final int STATE_FOLLOWER = 1;// automatically apply packets
	public static final int STATE_NEITHER = 0;// default.
	public static final int DEFAULT_PORT = 80;// my favorite port
	final static Logger logger = Logger.getLogger(JabberUtils.class.getName());

	public static boolean login(XMPPConnection conn, String username,
			String password) {

		try {
			// XXX until smack lib gets updated..
			conn.connect();
			conn.login(username, password);

		} catch (XMPPException e) {
			logger.severe("could not connect to XMPPServer");
			// let world know know about error (delete?)
			e.printStackTrace();
			// let calling method know
			return false;
		}

		return true;

	}

	public static XMPPConnection openConnection(String serverName) {
		XMPPConnection conn1 = null;
		// Create a connection to the named server.
		// try {
		// we are port 80 junkies
		ConnectionConfiguration connConfig = new ConnectionConfiguration(
				serverName, JabberUtils.DEFAULT_PORT);

		conn1 = new XMPPConnection(connConfig);

		logger.info("xmpp connected " + conn1.isConnected());
		logger.info("xmpp using TLS " + conn1.isSecureConnection());
		// } catch (XMPPException e) {
		// TODO Auto-generated catch block

		// }
		return conn1;
	}

	public static boolean createAccount(XMPPConnection conn, String username,
			String password) {
		AccountManager mngr = new AccountManager(conn);
		try {
			mngr.createAccount(username, password);

		} catch (XMPPException e) {
			// let world know know about error (delete?)
			e.printStackTrace();
			// let calling method know
			return false;
		}

		return true;
	}

	public static boolean addRosterEntry(XMPPConnection conn, String user,
			String name) {
		Roster rost = conn.getRoster();
		try {
			rost.createEntry(user, name, null);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static DefaultPacketExtension makeSelectionExtension(int[] selection) {
		DefaultPacketExtension ext = new DefaultPacketExtension(
				JabberUtils.SELECTION_ELEMENT_NAME,
				JabberUtils.DEFAULT_NAMESPACE);
		for (int i = 0; i < selection.length; i++) {
			// d for data. we need to start with a string, else connection
			// closes on sending
			ext.setValue("D" + Integer.toString(i), Integer
					.toString(selection[i]));
		}
		ext.setValue(JabberUtils.LENGTH_STRING, Integer
				.toString(selection.length));
		return ext;
	}

	public static int[] getSelection(DefaultPacketExtension ext) {
		String length = ext.getValue(JabberUtils.LENGTH_STRING);
		Integer len = Integer.valueOf(length);
		int[] vals = new int[len.intValue()];
		for (int i = 0; i < vals.length; i++) {
			String val = ext.getValue("D" + i);
			int intVal = Integer.valueOf(val).intValue();
			vals[i] = intVal;
		}

		return vals;
	}

	public static DefaultPacketExtension makeSubspaceExtension(int[] subspace) {
		DefaultPacketExtension ext = new DefaultPacketExtension(
				JabberUtils.SUBSPACE_ELEMENT_NAME,
				JabberUtils.DEFAULT_NAMESPACE);
		for (int i = 0; i < subspace.length; i++) {
			// d for data. we need to start with a string, else connection
			// closes on sending
			ext.setValue("D" + Integer.toString(i), Integer
					.toString(subspace[i]));
		}
		ext.setValue(JabberUtils.LENGTH_STRING, Integer
				.toString(subspace.length));
		return ext;
	}

	public static int[] getSubspace(DefaultPacketExtension ext) {
		String length = ext.getValue(JabberUtils.LENGTH_STRING);
		Integer len = Integer.valueOf(length);
		int[] vals = new int[len.intValue()];
		for (int i = 0; i < vals.length; i++) {
			String val = ext.getValue("D" + i);
			int intVal = Integer.valueOf(val).intValue();
			vals[i] = intVal;
		}
		return vals;
	}

	public static DefaultPacketExtension makeMarshaledComponentExtension(
			String componentXML) {
		DefaultPacketExtension ext = new DefaultPacketExtension(
				JabberUtils.MARSHALED_COMPONENT_ELEMENT_NAME,
				JabberUtils.DEFAULT_NAMESPACE);

		ext.setValue(JabberUtils.MARSHALLED_COMPONENT_XML_STRING, "xml:"
				+ componentXML);
		logger.info("original component:" + componentXML);
		logger.info("packet:" + ext.toXML());
		return ext;
	}

	public static String getMarshaledComponent(DefaultPacketExtension ext) {
		logger.info("ext:" + ext.toXML());

		String componentXML = ext
				.getValue(JabberUtils.MARSHALLED_COMPONENT_XML_STRING);
		componentXML = componentXML.substring(4);
		logger.info("componentXML:" + componentXML);
		return componentXML;

	}

	public static DefaultPacketExtension makeSpatialExtentExtension(
			Rectangle2D spatialExtent) {
		DefaultPacketExtension ext = new DefaultPacketExtension(
				JabberUtils.SPATIAL_EXTENT_ELEMENT_NAME,
				JabberUtils.DEFAULT_NAMESPACE);
		ext.setValue("width", Double.toString(spatialExtent.getWidth()));
		ext.setValue("height", Double.toString(spatialExtent.getHeight()));
		ext.setValue("x", Double.toString(spatialExtent.getX()));
		ext.setValue("y", Double.toString(spatialExtent.getY()));
		return ext;
	}

	public static Rectangle2D getSpatialExtent(DefaultPacketExtension ext) {
		double x, y, width, height;
		x = Double.valueOf(ext.getValue("x")).doubleValue();
		y = Double.valueOf(ext.getValue("y")).doubleValue();
		width = Double.valueOf(ext.getValue("width")).doubleValue();
		height = Double.valueOf(ext.getValue("height")).doubleValue();

		return new Rectangle2D.Double(x, y, width, height);
	}

	public static DefaultPacketExtension makeMessageExtension(String message) {
		DefaultPacketExtension ext = new DefaultPacketExtension(
				JabberUtils.MESSAGE_ELEMENT_NAME, JabberUtils.DEFAULT_NAMESPACE);

		ext.setValue(JabberUtils.MESSAGE_BODY_STRING, message);
		return ext;
	}

	public static String getMessage(DefaultPacketExtension ext) {
		String msg = ext.getValue(JabberUtils.MESSAGE_BODY_STRING);
		return msg;
	}

}
