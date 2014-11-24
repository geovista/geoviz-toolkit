/* -------------------------------------------------------------------
 Java source file for the class SocketClient
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SocketClient.java,v 1.9 2006/02/27 19:28:41 hardisty Exp $
 $Date: 2006/02/27 19:28:41 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation under
 version 2.1 of the License.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.collaboration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

class SocketClient {
	final static Logger logger = Logger.getLogger(SocketClient.class.getName());

	public static void main(String args[]) throws Exception {
		String sentence;
		String host = "194.129.258.70";

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));

		Socket clientSocket = new Socket(host, 80);
		DataOutputStream outToServer = null;
		BufferedInputStream inFromServer = null;

		try {

			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedInputStream(clientSocket
					.getInputStream());

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ host);
		}

		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');

		FileOutputStream fos = new FileOutputStream("c://6meg.pdf");

		int totalDataRead;
		int totalSizeWritten = 0;
		int DATA_SIZE = 20480;
		byte[] inData = new byte[DATA_SIZE];

		logger.info("Begin");

		while ((totalDataRead = inFromServer.read(inData, 0, inData.length)) >= 0) {
			fos.write(inData, 0, totalDataRead);
			totalSizeWritten = totalSizeWritten + totalDataRead;
			logger.info("" + totalSizeWritten);
		}

		logger.info("Done");
		fos.close();
		clientSocket.close();

	}
}
