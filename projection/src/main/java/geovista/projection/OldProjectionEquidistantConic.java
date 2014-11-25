/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.projection;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

import geovista.common.data.GeneralPathLine;

/**
 * Equidistant Conic projection, useful for reasonably large sections of the
 * United States, or other contries in similar latitudes.
 */
public class OldProjectionEquidistantConic implements OldProjection {
	protected final static Logger logger = Logger
			.getLogger(OldProjectionEquidistantConic.class.getName());
	private double centralMeridian = Double.NaN;
	private double standardParallelOne = Double.NaN;
	private double standardParallelTwo = Double.NaN;
	private double centralLatitude = Double.NaN;

	/*
	 * Coordinates should be passed in as radians. The projected values are
	 * returned in radians as well. The second argument can be null, or not. If
	 * it is not, that Point2D will returned by the method
	 */
	public Point2D.Double project(double lat, double longVal, Point2D.Double pt) {
		if (Double.isNaN(centralLatitude) || Double.isNaN(centralMeridian)
				|| Double.isNaN(standardParallelOne)
				|| Double.isNaN(standardParallelOne)) {
			throw new IllegalStateException(
					"OldProjectionEquidistantConic needs to have "
							+ "member variables set.");
		}

		if (pt == null) {
			pt = new Point2D.Double();
		}

		OldProjectionEquidistantConic.projectLongitudeLatititudeRadians(
				longVal, lat, centralMeridian, standardParallelOne,
				standardParallelTwo, centralLatitude, pt);

		// pt.setLocation(longVal, lat);
		return pt;
	}

	/*
	 * array[0] = X array[1] = Y
	 */
	public static void projectLongitudeLatititudeRadians(
			double longitudeRadians, double latitudeRadians,
			double centralMeridian, double standardParallelOne,
			double standardParallelTwo, double centralLatitude,
			Point2D.Double pt) {
		// This small check re-adjusts all longitude values depending on the
		// placement of the central meridian.
		if (centralMeridian < 0) {
			// A check to see if each longitude value is west of the central
			// meridian.
			// Thus, those meridians in the eastern hemisphere that are greater
			// than pi distance away from
			// the central meridian are subtracted from 2 * pi. These longitude
			// values are now within -pi to +pi ranage.
			if (Math.abs(longitudeRadians - centralMeridian) > Math.PI) {
				// Using -90 as the central meridian
				// e.g., Abs(100 - (-90)) = 190; which is greater than 180
				longitudeRadians = longitudeRadians - (2 * Math.PI);

				// Thus, (100 - 360) = -260; which is within the +pi to -pi
				// range.
			}
		}

		if (centralMeridian > 0) {
			// A check to see if each longitude value is east of the central
			// meridian.
			// Thus, those meridians in the western hemisphere that are greater
			// than pi distance away from
			// the central meridian are added to 2 * pi. These longitude values
			// are now within -pi to +pi ranage.
			if ((longitudeRadians - centralMeridian) > Math.PI) {
				// Using 90 as the central meridian
				// e.g., Abs(-100 - (90)) = 190; which is greater than 180
				longitudeRadians = longitudeRadians + (2 * Math.PI);

				// Thus, (-100 + 360) = 260; which is within the +pi to -pi
				// range.
			}
		}

		double eQConic_n;
		double eQConic_G;
		double eQConic_P;
		double eQConic_Po;
		double eQConic_Theta;

		// The first step in calculating the Equidistant Conic x and y plotting
		// coordinates
		// requires solving for several intermediate values.
		eQConic_n = (Math.cos(standardParallelOne) - Math
				.cos(standardParallelTwo))
				/ (standardParallelTwo - standardParallelOne);
		eQConic_G = (Math.cos(standardParallelOne) / eQConic_n)
				+ standardParallelOne;
		eQConic_Po = 0.65 * (eQConic_G - centralLatitude);
		eQConic_P = 0.65 * (eQConic_G - latitudeRadians);
		eQConic_Theta = eQConic_n * (longitudeRadians - centralMeridian);

		// Defining the Equidistant Conic projection plotting coordinates
		double eQConic_Y = eQConic_Po - (eQConic_P * (Math.cos(eQConic_Theta)));
		double eQConic_X = eQConic_P * Math.sin(eQConic_Theta);

		// double[] xY = new double[2];
		pt.x = eQConic_X;
		pt.y = eQConic_Y;

		// return pt;
	}

	public void setCentralMeridian(double centralMeridian) {
		this.centralMeridian = centralMeridian;
	}

	public double getCentralMeridian() {
		return centralMeridian;
	}

	public void setStandardParallelOne(double standardParallelOne) {
		this.standardParallelOne = standardParallelOne;
	}

	public double getStandardParallelOne() {
		return standardParallelOne;
	}

	public void setStandardParallelTwo(double standardParallelTwo) {
		this.standardParallelTwo = standardParallelTwo;
	}

	public double getStandardParallelTwo() {
		return standardParallelTwo;
	}

	public void setCentralLatitude(double centralLatitude) {
		this.centralLatitude = centralLatitude;
	}

	public double getCentralLatitude() {
		return centralLatitude;
	}

	// XXX this method does not do anything
	public Shape transform(Shape shpIn) {
		// PathIterator pit = shpIn.getPathIterator(null);
		// GeneralPath gep = new GeneralPath(shpIn);

		logger.info("hi i'm here! but why?");

		return shpIn;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param s
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Shape project(Shape s) {

		GeneralPath p = new GeneralPath();

		float[] coords = { 0, 0, 0, 0, 0, 0 };
		float first_x = 0;
		float first_y = 0;
		double x = 0;
		double y = 0;
		Point2D.Double pt = new Point2D.Double();

		for (PathIterator iter = s.getPathIterator(null); !iter.isDone(); iter
				.next()) {
			switch (iter.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				first_x = coords[0];
				first_y = coords[1];
				x = Math.toRadians(first_x);
				y = Math.toRadians(first_y);

				this.project(y, x, pt);

				first_x = (float) pt.getX();
				first_y = (float) pt.getY();
				p.moveTo(first_x, first_y);
				break;

			case PathIterator.SEG_LINETO:
				first_x = coords[0];
				first_y = coords[1];
				x = Math.toRadians(first_x);
				y = Math.toRadians(first_y);
				this.project(y, x, pt);

				first_x = (float) pt.getX();
				first_y = (float) pt.getY();
				p.lineTo(first_x, first_y);
				break;

			case PathIterator.SEG_QUADTO:
				throw new IllegalArgumentException(
						"OldProjection doesn't know what to do with this");

			case PathIterator.SEG_CUBICTO:
				throw new IllegalArgumentException(
						"OldProjection doesn't know what to do with this");

			case PathIterator.SEG_CLOSE:
				p.closePath();
				break;
			}
		}
		Shape sh = null;
		if (s instanceof GeneralPathLine) {
			sh = new GeneralPathLine(p);
		} else {
			sh = p;
		}
		return sh;
	}
}
