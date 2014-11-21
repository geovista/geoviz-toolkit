/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.Arrays;
import java.util.logging.Logger;

public class ProbabilityDensity {

	private static final Logger logger = Logger
			.getLogger(ProbabilityDensity.class.getName());

	private final double[] values;

	public ProbabilityDensity(double[] values) {
		Arrays.sort(values);
		this.values = values;
	}

	public double[] getUnderlyingArray() {
		return values;
	}

	public double findPValue(double givenValue) {

		return DescriptiveStatistics.percentAbovePresort(values, givenValue);
	}

}
