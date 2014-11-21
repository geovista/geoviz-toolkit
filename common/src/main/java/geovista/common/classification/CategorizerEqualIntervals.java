/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.classification;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CategorizerEqualIntervals extends BasicCategorizer {

	protected final static Logger logger = Logger
			.getLogger(CategorizerEqualIntervals.class.getName());

	public CategorizerEqualIntervals() {
		classifer = new ClassifierEqualIntervals();

	}

	@Override
	protected void setCategorygetBoundary(CategoryList ctgList,
			double[] rawData, int[] classedData) {
		double[] boundaries = ((ClassifierEqualIntervals) classifer)
				.getEqualBoundaries(rawData, ctgList.getNumberOfCategory());
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("boundaries:" + boundaries);
		}
		for (int i = 0; i < ctgList.getNumberOfCategory(); i++) {

			Category ctg = ctgList.getCategoryByID(i);
			if (ctg != null) {
				ctg.setMin((float) boundaries[i]);
				ctg.setMax((float) boundaries[i + 1]);
			}
		}
	}

}