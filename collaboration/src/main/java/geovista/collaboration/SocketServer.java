/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.collaboration;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

class SocketServer {
	final static Logger logger = Logger.getLogger(SocketServer.class.getName());

	public static void main(String args[]) throws Exception {

		ServerSocket welcomeSocket = null;

		try {
			welcomeSocket = new ServerSocket(80);
		} catch (IOException ioe) {
			logger.info("Could not listen on port: 80");
			System.exit(1);
		}

		while (true) {
			Socket connectionSocket = null;

			try {
				connectionSocket = welcomeSocket.accept();
			} catch (IOException ioe) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			BufferedOutputStream outToClient = new BufferedOutputStream(
					connectionSocket.getOutputStream());
			logger.info(inFromClient.readLine());

			int totalSizeTransferred = 0;
			int totalSizeRead;
			int PACKET_SIZE = 20480;
			byte[] packet = new byte[PACKET_SIZE];

			logger.info("reading file...");
			FileInputStream fis = new FileInputStream("6meg.pdf");

			while ((totalSizeRead = fis.read(packet, 0, packet.length)) >= 0) {
				outToClient.write(packet, 0, totalSizeRead);
				totalSizeTransferred = totalSizeTransferred + totalSizeRead;
				logger.info("" + totalSizeTransferred);
			}

			logger.info("done reading file...");
			outToClient.close();
			fis.close();
		}
	}

}
