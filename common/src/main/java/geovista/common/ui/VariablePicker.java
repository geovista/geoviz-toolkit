/* -------------------------------------------------------------------
 Java source file for the class VariablePicker
 Copyright (c), 2005 Aaron Myers
 $Author: hardisty $
 $Id: VariablePicker.java,v 1.3 2005/03/24 20:39:59 hardisty Exp $
 $Date: 2005/03/24 20:39:59 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.common.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;

public class VariablePicker extends JPanel implements DataSetListener,
		SubspaceListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton sendButton;
	String[] varNames;
	JList varList;
	final static Logger logger = Logger.getLogger(VariablePicker.class
			.getName());
	DataSetForApps dataSet;
	
	// added by Peter Foley to allow specific variables types in the variable picker
	// by default all (numeric types) are allowed replicating previous behaviour
	private int NUMERIC_TYPE = DataSetForApps.NULL_INT_VALUE;

	// Creates Variable Picker
	public VariablePicker() {
		super();
		varList = new JList();
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		sendButton = new JButton("Send Selection");
		this.add(sendButton, BorderLayout.NORTH);
		sendButton.addActionListener(this);
		JScrollPane scrollPane = new JScrollPane();
		JViewport scrollView = new JViewport();
		scrollView.add(varList);
		scrollPane.setViewport(scrollView);
		scrollPane.getViewport();
		this.add(scrollPane, BorderLayout.CENTER);
		varList.setVisibleRowCount(10);
		if (logger.isLoggable(Level.FINEST) && varNames != null) {
			logger.finest("the first attribute is " + varNames[0]);
		}
	}
	
	/*
	 * Additional Constructor added by Peter Foley to allow specific variable types
	 *  to be specified for the Variable Picker to display
	 */
	
	public VariablePicker(int varNumericType ) {
		
		// call the default no argument constructor
		this();
		
		// check to make sure that the variable type is supported by the DataSetForApps class
		if ( varNumericType == DataSetForApps.TYPE_BOOLEAN || 
				varNumericType == DataSetForApps.TYPE_INTEGER || 
				varNumericType == DataSetForApps.TYPE_DOUBLE) {
			NUMERIC_TYPE = varNumericType;
		}
	}

	// Adds Dataset Changed Component
	public void dataSetChanged(DataSetEvent e) {
		dataSet = e.getDataSetForApps();

		
		// extract the numeric variable names
		DataSetForApps tempDApp = new DataSetForApps(e.getDataSet());
		String[] newVarNames = setVariableNames(tempDApp);
				
		varList.setListData(newVarNames);
		varList.repaint();
	}
	
	/*
	 * Extract the numeric variable names from the input DataSetForApps instance
	 * By default, this method returns a list of numeric variable names (double,int,boolean)
	 * If NUMERIC_TYPE is specified then it will return a list of numeric variable names corresponding
	 * to that type
	 */
	private String[] setVariableNames(DataSetForApps data) {
		
		String[] newVarNames = null;
		
		if (NUMERIC_TYPE != DataSetForApps.NULL_INT_VALUE) {
			
			int [] dataTypes = data.getDataTypeArray();
			List<String> tempNewVarNames= new ArrayList<String>();
			for ( int i = 0; i < dataTypes.length; i++ ) {
				if ( dataTypes[i] == NUMERIC_TYPE ) {
					tempNewVarNames.add(data.getColumnName(i));
				}
			}
			newVarNames = new String[tempNewVarNames.size()];
			tempNewVarNames.toArray(newVarNames);
			
		} else {
			newVarNames = data.getAttributeNamesNumeric();
		}
				
		return newVarNames;
	}

	// Adds Action Perfromed Event
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			fireSubspaceChanged(varList.getSelectedIndices());
		}
	}

	// Add Subspace Changed Event
	public void subspaceChanged(SubspaceEvent e) {
		int[] subspace = e.getSubspace();
		varList.setSelectedIndices(subspace);
	}

	// Fires Subspace Changed
	public void fireSubspaceChanged(int[] selection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SubspaceEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SubspaceListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SubspaceEvent(this, selection);
				}

				((SubspaceListener) listeners[i + 1]).subspaceChanged(e);
			}
		}
	}

	// Add Subspace Changed Listener
	public void addSubspaceListener(SubspaceListener l) {
		listenerList.add(SubspaceListener.class, l);
	}

	// Removes that Listener
	public void removeSubspaceListener(SubspaceListener l) {
		listenerList.remove(SubspaceListener.class, l);
	}

	public static void main2(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		VisualSettingsMenuClientExample client = new VisualSettingsMenuClientExample();
		app.add(client);
		app.setVisible(true);
	}

	// Adds Variable Picker to a JFrame
	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout flow = new FlowLayout();
		app.getContentPane().setLayout(flow);
		VariablePicker vp = new VariablePicker();
		VariablePicker vp2 = new VariablePicker();
		JScrollPane scrollPane = new JScrollPane();
		JScrollPane scrollPane2 = new JScrollPane();
		JViewport scrollView = new JViewport();
		JViewport scrollView2 = new JViewport();
		vp.addSubspaceListener(vp2);
		vp2.addSubspaceListener(vp);
		scrollView.add(vp);
		scrollView2.add(vp2);
		scrollPane.setViewport(scrollView);
		scrollPane2.setViewport(scrollView2);
		scrollPane.getViewport();
		scrollPane2.getViewport();
		app.getContentPane().add(scrollPane);
		app.getContentPane().add(scrollPane2);
		app.pack();
		app.setVisible(true);
	}

}
