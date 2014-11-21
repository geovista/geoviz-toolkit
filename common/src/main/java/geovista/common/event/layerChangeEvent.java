/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Bonan Li */

package geovista.common.event;

import java.io.File;
import java.util.EventObject;

import geovista.common.data.DataSetForApps;

public class layerChangeEvent extends EventObject {

	private final int activateIdx;
	private final int removedIdx;
	private final File activateFile;
	private final DataSetForApps dataSetForApp;

	public layerChangeEvent(Object source, int activateIdx, int removedIdx,
			File activateFile, DataSetForApps dataSetForApp) {
		super(source);
		this.activateIdx = activateIdx;
		this.removedIdx = removedIdx;
		this.activateFile = activateFile;
		this.dataSetForApp = dataSetForApp;
	}

	public int getActivatedIdx() {
		return activateIdx;
	}

	public int getRemovedIdx() {
		return removedIdx;
	}

	public File getActivateFile() {
		return activateFile;
	}

	public DataSetForApps getDataSetForApp() {
		return dataSetForApp;
	}
}