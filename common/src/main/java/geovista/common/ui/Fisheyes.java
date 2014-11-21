/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jean-Daniel Fekete and Frank Hardisty */
package geovista.common.ui;

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 * Fisheyes manage space deformation to maintain focus+context views by applying
 * a space deformation. See Sheelagh Carpendale's PhD for full details.
 * 
 * @author Jean-Daniel Fekete
 * 
 */
public class Fisheyes {
	/** constant value for setDistanceMetric to use a L1 distance */
	public static final short DISTANCE_L1 = 0;
	/** constant value for setDistanceMetric to use a L2 distance */
	public static final short DISTANCE_L2 = 1;
	/** constant value for setDistanceMetric to use a L infinity distance */
	public static final short DISTANCE_LINF = 2;
	/** constant value for setLensType to use a gaussian lens types */
	public static final short LENS_GAUSSIAN = 0;
	/** constant value for setLensType to use a cosine lens types */
	public static final short LENS_COSINE = 1;
	/** constant value for setLensType to use a hemisphere lens types */
	public static final short LENS_HEMISPHERE = 2;
	/** constant value for setLensType to use a linear lens types */
	public static final short LENS_LINEAR = 3;
	/** constant value for setLensType to use an inverse cosine lens types */
	public static final short LENS_INVERSE_COSINE = 4;
	/** The virtual camera height is 10.0f */
	public static final float referenceHeight = 10.0f;
	/** The virtual viewplane is located at this distance from the camera */
	public static final float distanceViewplance = 1.0f;
	transient Rectangle2D.Float bounds = new Rectangle2D.Float();
	float focusX;
	float focusY;
	float lensRadius;
	float focusRadius;
	float focalHeight;
	float tolerance = 1;
	short distanceMetric;
	Metric metric;
	short lensType;
	LensProfile lensProfile;

	/**
	 * Constructor for Fisheyes.
	 */
	public Fisheyes() {
		this(100, 0, 9);
	}

	/**
	 * Creates a new Fisheye object.
	 * 
	 * @param lensRadius
	 *            the lens radius.
	 * @param focusRadius
	 *            DOCUMENT ME!
	 * @param focalHeight
	 *            the focal heigt (0 &lt;= 9)
	 */
	public Fisheyes(float lensRadius, float focusRadius, float focalHeight) {
		setLensRadius(lensRadius);
		setFocusRadius(focusRadius);
		setFocalHeight(focalHeight);
		setDistanceMetric(DISTANCE_L2);
		setLensType(LENS_LINEAR);
	}

	public Rectangle2D getBounds() {
		if (bounds == null) {
			bounds = new Rectangle2D.Float(focusX - focusRadius, focusY
					- focusRadius, 2 * focusRadius, 2 * focusRadius);
		}
		return bounds;
	}

	/**
	 * Returns true of point is transformed.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * 
	 * @return true of point is transformed.
	 */
	public boolean isTransformed(float x, float y) {
		return metric.compare(lensRadius, x - focusX, y - focusY) > 0;
	}

	/**
	 * Returns true of point is transformed.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * 
	 * @return true of point is transformed.
	 */
	public boolean isTransformed(double x, double y) {
		return isTransformed((float) x, (float) y);
	}

	/**
	 * Returns true of point is transformed.
	 * 
	 * @param p
	 *            the point
	 * 
	 * @return true of point is transformed.
	 */
	public boolean isTransformed(Point2D p) {
		return isTransformed(p.getX(), p.getY());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param bounds
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isTransformed(Rectangle2D bounds) {
		return bounds.intersects(getBounds());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param s
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isTransformed(Shape s) {
		return getBounds().intersects(s.getBounds2D());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param s
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Shape transform(Shape s) {
		if (!isTransformed(s)) {
			return s;
		}

		GeneralPath p = new GeneralPath();
		float[] coords = { 0, 0, 0, 0, 0, 0 };
		float first_x = 0;
		float first_y = 0;
		float first_tx = 0;
		float first_ty = 0;
		float prev_x = 0;
		float prev_y = 0;
		float prev_tx = 0;
		float prev_ty = 0;
		for (PathIterator iter = s.getPathIterator(null); !iter.isDone(); iter
				.next()) {
			switch (iter.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				prev_x = coords[0];
				prev_y = coords[1];
				first_x = prev_x;
				first_y = prev_y;
				transform(coords, 1);
				prev_tx = coords[0];
				prev_ty = coords[1];
				first_tx = prev_tx;
				first_ty = prev_ty;
				p.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO: {
				float x = coords[0];
				float y = coords[1];
				transform(coords, 1);
				subdivide(prev_x, prev_y, prev_tx, prev_ty, x, y, coords[0],
						coords[1], p);
				prev_x = x;
				prev_y = y;
				prev_tx = coords[0];
				prev_ty = coords[1];
				break;
			}
			case PathIterator.SEG_QUADTO: {
				float x1 = coords[0];
				float y1 = coords[1];
				float x2 = coords[2];
				float y2 = coords[3];
				transform(coords, 2);
				subdivide(prev_x, prev_y, prev_tx, prev_ty, x1, y1, coords[0],
						coords[1], x2, y2, coords[2], coords[3], p);
				prev_x = x2;
				prev_y = y2;
				prev_tx = coords[2];
				prev_ty = coords[3];
				break;
			}
			case PathIterator.SEG_CUBICTO: {
				float x1 = coords[0];
				float y1 = coords[1];
				float x2 = coords[2];
				float y2 = coords[3];
				float x3 = coords[4];
				float y3 = coords[5];
				transform(coords, 3);
				subdivide(prev_x, prev_y, prev_tx, prev_ty, x1, y1, coords[0],
						coords[1], x2, y2, coords[2], coords[3], x3, y3,
						coords[4], coords[5], p);
				prev_x = x3;
				prev_y = y3;
				prev_tx = coords[4];
				prev_ty = coords[5];
				break;
			}
			case PathIterator.SEG_CLOSE: {
				subdivide(prev_x, prev_y, prev_tx, prev_ty, first_x, first_y,
						first_tx, first_ty, p);
				break;
			}
			}
		}
		return p;
	}

	void addTransformed(float x, float y, GeneralPath p) {
		float scale = getScale(x, y);
		float tx;
		float ty;
		if (scale == 1) {
			return;
		}
		tx = transformX(x, scale);
		ty = transformY(y, scale);
		p.lineTo(tx, ty);
	}

	static float dist2(float dx, float dy) {
		return dx * dx + dy * dy;
	}

	/**
	 * Subdivide a line segment.
	 * 
	 * @param x1
	 *            X coordinate of first point
	 * @param y1
	 *            Y coordinate of first point
	 * @param tx1
	 *            Y coordinate of first point
	 * @param ty1
	 *            transformed Y coordinate of first point
	 * @param x2
	 *            X coordinate of second point
	 * @param y2
	 *            Y coordinate of second point
	 * @param tx2
	 *            Y coordinate of second point
	 * @param ty2
	 *            transformed Y coordinate of second point
	 * @param p
	 *            GeneralPath to fill
	 */
	public void subdivide(float x1, float y1, float tx1, float ty1, float x2,
			float y2, float tx2, float ty2, GeneralPath p) {
		float xm = (x1 + x2) / 2;
		float ym = (y1 + y2) / 2;
		float scale = getScale(xm, ym);
		float txm = transformX(xm, scale);
		float tym = transformY(ym, scale);

		if (dist2(txm - (tx1 + tx2) / 2, tym - (ty1 + ty2) / 2) > tolerance) {
			subdivide(x1, y1, tx1, ty2, xm, ym, txm, tym, p);
			p.lineTo(txm, tym);
			subdivide(xm, ym, txm, tym, x2, y2, tx2, ty2, p);
		} else {
			p.lineTo(tx2, ty2);
		}
	}

	private final QuadCurve2D.Float leftQuad = new QuadCurve2D.Float();
	private final QuadCurve2D.Float rightQuad = new QuadCurve2D.Float();

	/**
	 * Subdivide a quad segment.
	 * 
	 * @param x1
	 *            X coordinate of first point
	 * @param y1
	 *            Y coordinate of first point
	 * @param tx1
	 *            Y coordinate of first point
	 * @param ty1
	 *            transformed Y coordinate of first point
	 * @param x2
	 *            X coordinate of second point
	 * @param y2
	 *            Y coordinate of second point
	 * @param tx2
	 *            Y coordinate of second point
	 * @param ty2
	 *            transformed Y coordinate of second point
	 * @param x3
	 *            X coordinate of third point
	 * @param y3
	 *            Y coordinate of third point
	 * @param tx3
	 *            Y coordinate of third point
	 * @param ty3
	 *            transformed Y coordinate of third point
	 * @param p
	 *            GeneralPath to fill
	 */
	public void subdivide(float x1, float y1, float tx1, float ty1, float x2,
			float y2, float tx2, float ty2, float x3, float y3, float tx3,
			float ty3, GeneralPath p) {
		leftQuad.setCurve(tx1, ty1, tx2, ty2, tx3, ty3);
		if (leftQuad.getFlatnessSq() <= tolerance) {
			p.lineTo(tx3, ty3);
			return;
		}
		leftQuad.setCurve(x1, y1, x2, y2, x3, y3);
		leftQuad.subdivide(leftQuad, rightQuad);
		float scaleLeft = getScale(leftQuad.ctrlx, leftQuad.ctrly);
		float txLeft = transformX(leftQuad.ctrlx, scaleLeft);
		float tyLeft = transformY(leftQuad.ctrly, scaleLeft);
		float scaleM = getScale(leftQuad.x2, leftQuad.y2);
		float txm = transformX(leftQuad.x2, scaleM);
		float tym = transformY(leftQuad.y2, scaleM);
		subdivide(x1, y1, tx1, ty1, leftQuad.ctrlx, leftQuad.ctrly, txLeft,
				tyLeft, leftQuad.x2, leftQuad.y2, txm, tym, p);
		p.lineTo(txm, tym);
		float scaleRight = getScale(rightQuad.ctrlx, rightQuad.ctrly);
		float txRight = transformX(rightQuad.ctrlx, scaleRight);
		float tyRight = transformY(rightQuad.ctrly, scaleRight);
		subdivide(rightQuad.x1, rightQuad.y1, txm, tym, rightQuad.ctrlx,
				rightQuad.ctrly, txRight, tyRight, x3, y3, tx3, ty3, p);
	}

	private final CubicCurve2D.Float leftCubic = new CubicCurve2D.Float();
	private final CubicCurve2D.Float rightCubic = new CubicCurve2D.Float();

	/**
	 * Subdivide a quad segment.
	 * 
	 * @param x1
	 *            X coordinate of first point
	 * @param y1
	 *            Y coordinate of first point
	 * @param tx1
	 *            Y coordinate of first point
	 * @param ty1
	 *            transformed Y coordinate of first point
	 * @param x2
	 *            X coordinate of second point
	 * @param y2
	 *            Y coordinate of second point
	 * @param tx2
	 *            Y coordinate of second point
	 * @param ty2
	 *            transformed Y coordinate of second point
	 * @param x3
	 *            X coordinate of third point
	 * @param y3
	 *            Y coordinate of third point
	 * @param tx3
	 *            Y coordinate of third point
	 * @param ty3
	 *            transformed Y coordinate of third point
	 * @param x3
	 *            X coordinate of fourth point
	 * @param y3
	 *            Y coordinate of fourth point
	 * @param tx3
	 *            Y coordinate of fourth point
	 * @param ty3
	 *            transformed Y coordinate of fourth point
	 * @param p
	 *            GeneralPath to fill
	 */
	public void subdivide(float x1, float y1, float tx1, float ty1, float x2,
			float y2, float tx2, float ty2, float x3, float y3, float tx3,
			float ty3, float x4, float y4, float tx4, float ty4, GeneralPath p) {
		leftCubic.setCurve(tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4);
		if (leftCubic.getFlatnessSq() <= tolerance) {
			p.lineTo(tx4, ty4);
			return;
		}
		leftCubic.setCurve(x1, y1, x2, y2, x3, y3, x4, y4);
		leftCubic.subdivide(leftCubic, rightCubic);
		float scale = getScale(leftCubic.ctrlx1, leftCubic.ctrly1);
		float tctrlx1 = transformX(leftCubic.ctrlx1, scale);
		float tctrly1 = transformY(leftCubic.ctrly1, scale);
		scale = getScale(leftCubic.ctrlx2, leftCubic.ctrly2);
		float tctrlx2 = transformX(leftCubic.ctrlx2, scale);
		float tctrly2 = transformY(leftCubic.ctrly2, scale);
		scale = getScale(leftCubic.x2, leftCubic.y2);
		float txm = transformX(leftCubic.x2, scale);
		float tym = transformY(leftCubic.y2, scale);
		subdivide(x1, y1, tx1, ty1, leftCubic.ctrlx1, leftCubic.ctrly1,
				tctrlx1, tctrly1, leftCubic.ctrlx2, leftCubic.ctrly2, tctrlx2,
				tctrly2, leftCubic.x2, leftCubic.y2, txm, tym, p);
		p.lineTo(txm, tym);
		scale = getScale(rightCubic.ctrlx1, rightCubic.ctrly1);
		tctrlx1 = transformX(rightCubic.ctrlx1, scale);
		tctrly1 = transformY(rightCubic.ctrly1, scale);
		scale = getScale(rightCubic.ctrlx2, rightCubic.ctrly2);
		tctrlx2 = transformX(rightCubic.ctrlx2, scale);
		tctrly2 = transformY(rightCubic.ctrly2, scale);
		subdivide(rightCubic.x1, rightCubic.y1, txm, tym, rightCubic.ctrlx1,
				rightCubic.ctrly1, tctrlx1, tctrly1, rightCubic.ctrlx2,
				rightCubic.ctrly2, tctrlx2, tctrly2, x3, y3, tx3, ty3, p);
	}

	/**
	 * Returns the height of a specified point.
	 * 
	 * @param x
	 *            X coordinate of the point
	 * @param y
	 *            Y coordinate of the point
	 * 
	 * @return the height of the specified point.
	 */
	public float pointHeight(float x, float y) {
		return height(distance(x, y));
	}

	/**
	 * Sets for focus position
	 * 
	 * @param x
	 *            X coordinate of the position
	 * @param y
	 *            X coordinate of the position
	 */
	public void setFocus(float x, float y) {
		focusX = x;
		focusY = y;
		bounds.x = x - lensRadius;
		bounds.y = y - lensRadius;
	}

	/**
	 * Returns the distance of the specified point from the focus.
	 * 
	 * @param x
	 *            X coordinate of the point
	 * @param y
	 *            Y coordinate of the point
	 * 
	 * @return the distance of the specified point from the focus.
	 */
	public float distance(float x, float y) {
		return metric.distance(x - focusX, y - focusY);
	}

	/**
	 * Returns the height at the specified distance from the focus
	 * 
	 * @param dist
	 *            the distance
	 * 
	 * @return the height at the specified distance from the focus
	 */
	public float height(float dist) {
		if (focalHeight == 0) {
			return 0;
		}

		float realFocus = focusRadius / getMaximumScale();
		if (dist > lensRadius) {
			return 0;
		} else if (dist <= realFocus) {
			return focalHeight;
		} else {
			float t = (dist - realFocus) / (lensRadius - realFocus);
			return Math.min(focalHeight * lens(t), focalHeight);
		}
	}

	/**
	 * Returns the height at the specified normalized distance from the focus
	 * 
	 * @param t
	 *            the normalized distance from the focus
	 * 
	 * @return the height at the specified normalized distance from the focus
	 */
	public float lens(float t) {
		return lensProfile.profile(t);
	}

	/**
	 * Returns the focusX.
	 * 
	 * @return float
	 */
	public float getFocusX() {
		return focusX;
	}

	/**
	 * Returns the focusY.
	 * 
	 * @return float
	 */
	public float getFocusY() {
		return focusY;
	}

	/**
	 * Sets the focusX.
	 * 
	 * @param focusX
	 *            The focusX to set
	 */
	public void setFocusX(float focusX) {
		this.focusX = focusX;
		bounds.x = focusX - lensRadius;
	}

	/**
	 * Returns the lensRadius.
	 * 
	 * @return float
	 */
	public float getLensRadius() {
		return lensRadius;
	}

	/**
	 * Sets the focusY.
	 * 
	 * @param focusY
	 *            The focusY to set
	 */
	public void setFocusY(float focusY) {
		this.focusY = focusY;
		bounds.y = focusY - lensRadius;
	}

	/**
	 * Sets the lensRadius.
	 * 
	 * @param radius
	 *            The lensRadius to set
	 */
	public void setLensRadius(float radius) {
		lensRadius = radius;
		if (focusRadius > radius) {
			focusRadius = radius;
		}
		bounds.width = 2 * radius;
		bounds.height = 2 * radius;
		bounds.x = focusX - lensRadius;
		bounds.y = focusY - lensRadius;
	}

	/**
	 * Returns the focusRadius.
	 * 
	 * @return float
	 */
	public float getFocusRadius() {
		return focusRadius;
	}

	/**
	 * Sets the focusRadius.
	 * 
	 * @param focusRadius
	 *            The focusRadius to set
	 */
	public void setFocusRadius(float focusRadius) {
		this.focusRadius = focusRadius;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param focus
	 *            DOCUMENT ME!
	 * @param lens
	 *            DOCUMENT ME!
	 */
	public void setRadii(float focus, float lens) {
		if (lens < focus) {
			focus = lens;
		}
		focusRadius = focus;
		lensRadius = lens;
		bounds.width = 2 * lensRadius;
		bounds.height = 2 * lensRadius;
		bounds.x = focusX - lensRadius;
		bounds.y = focusY - lensRadius;
	}

	/**
	 * Returns the focal height.
	 * 
	 * @return float
	 */
	public float getFocalHeight() {
		return focalHeight;
	}

	/**
	 * Sets the focal height.
	 * 
	 * @param focalHeight
	 *            The focal height to set
	 */
	public void setFocalHeight(float focalHeight) {
		if (focalHeight < 0) {
			focalHeight = 0;
		} else if (focalHeight > 9) {
			focalHeight = 9;
		}

		this.focalHeight = focalHeight;
	}

	/**
	 * Change the maximum scale
	 * 
	 * @param scale
	 *            the new maximum scale
	 */
	public void setMaximumScale(float scale) {
		if (scale == 0) {
			setFocalHeight(0);
		} else {
			setFocalHeight(10 - (10 / scale));
		}
	}

	/**
	 * Returns the maximum scale
	 * 
	 * @return the maximum scale
	 */
	public float getMaximumScale() {
		return 10f / (10f - focalHeight);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param x
	 *            DOCUMENT ME!
	 * @param y
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float getScale(float x, float y) {
		float height = pointHeight(x, y);
		return 10f / (10f - height);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param x
	 *            DOCUMENT ME!
	 * @param scale
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float transformX(float x, float scale) {
		return (x - focusX) * scale + focusX;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param y
	 *            DOCUMENT ME!
	 * @param scale
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float transformY(float y, float scale) {
		return (y - focusY) * scale + focusY;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param coords
	 *            DOCUMENT ME!
	 */
	public void transform(float[] coords, int npoints) {
		for (int i = 0; i < npoints; i++) {
			float scale = getScale(coords[2 * i], coords[2 * i + 1]);
			if (scale != 1) {
				coords[2 * i] = transformX(coords[2 * i], scale);
				coords[2 * i + 1] = transformY(coords[2 * i + 1], scale);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param src
	 *            DOCUMENT ME!
	 * @param dst
	 *            DOCUMENT ME!
	 */
	public void transform(Point2D.Float src, Point2D.Float dst) {
		float scale = getScale(src.x, src.y);
		if (scale != 1) {
			dst.x = transformX(src.x, scale);
			dst.y = transformY(src.y, scale);
		} else if (dst != src) {
			dst.x = src.x;
			dst.y = src.y;
		}
	}

	/**
	 * Returns the distanceMetric.
	 * 
	 * @return short the distanceMetric
	 */
	public short getDistanceMetric() {
		return distanceMetric;
	}

	/**
	 * Returns the lensType.
	 * 
	 * @return short
	 */
	public short getLensType() {
		return lensType;
	}

	/**
	 * Sets the distanceMetric.
	 * 
	 * @param distanceMetrics
	 *            The distanceMetric to set
	 */
	public void setDistanceMetric(short distanceMetrics) {
		distanceMetric = distanceMetrics;

		switch (distanceMetrics) {
		case DISTANCE_L1:
			metric = new DistanceL1();
			break;
		case DISTANCE_L2:
			metric = new DistanceL2();
			break;
		case DISTANCE_LINF:
			metric = new DistanceLInf();
			break;
		}
	}

	/**
	 * Sets the lensType.
	 * 
	 * @param lensType
	 *            The lensType to set
	 */
	public void setLensType(short lensType) {
		this.lensType = lensType;

		switch (lensType) {
		case LENS_GAUSSIAN:
			lensProfile = new ProfileGuassian();
			break;
		case LENS_COSINE:
			lensProfile = new ProfileCos();
			break;
		case LENS_HEMISPHERE:
			lensProfile = new ProfileCos();
			break;
		case LENS_INVERSE_COSINE:
			lensProfile = new ProfileInverse(new ProfileCos());
			break;
		case LENS_LINEAR:
			lensProfile = new ProfileLinear();
			break;
		}
	}

	public interface Metric {
		public float distance(float dx, float dy);

		public int compare(float dist, float dx, float dy);
	}

	public interface LensProfile {
		public float profile(float t);
	}

	public static class ProfileCos implements LensProfile {
		public float profile(float t) {
			return (float) Math.cos(t * Math.PI / 2);
		}
	}

	static class ProfileGuassian implements LensProfile {
		static final double ro = 0.1;
		static final double denom = 1 / (ro * Math.sqrt(2 * Math.PI));

		public float profile(float t) {
			return (float) Math.exp((-t * t) / ro);
		}
	}

	static class ProfileOneMinusSin implements LensProfile {
		public float profile(float t) {
			return 1 - (float) Math.sin(t);
		}
	}

	static class ProfileLinear implements LensProfile {
		public float profile(float t) {
			return 1 - t;
		}
	}

	static class ProfileInverse implements LensProfile {
		LensProfile profile;

		public ProfileInverse(LensProfile profile) {
			this.profile = profile;
		}

		public float profile(float t) {
			return 1 - profile.profile(1 - t);
		}
	}

	static class DistanceL1 implements Metric {
		public float distance(float dx, float dy) {
			return Math.abs(dx) + Math.abs(dy);
		}

		public int compare(float dist, float dx, float dy) {
			float d = dist - distance(dx, dy);
			if (d < 0) {
				return -1;
			} else if (d == 0) {
				return 0;
			} else {
				return 1;
			}
		}

	}

	static class DistanceL2 implements Metric {
		public float distance(float dx, float dy) {

			return (float) Math.sqrt((dx * dx) + (dy * dy));
		}

		public int compare(float dist, float dx, float dy) {
			float d = dist * dist - (dx * dx) + (dy * dy);
			if (d < 0) {
				return -1;
			} else if (d == 0) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	static class DistanceLInf implements Metric {
		public float distance(float dx, float dy) {
			return Math.max(Math.abs(dx), Math.abs(dy));
		}

		public int compare(float dist, float dx, float dy) {
			float d = dist - distance(dx, dy);
			if (d < 0) {
				return -1;
			} else if (d == 0) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	/**
	 * Returns the tolerance.
	 * 
	 * @return float
	 */
	public float getTolerance() {
		return tolerance;
	}

	/**
	 * Sets the tolerance.
	 * 
	 * @param tolerance
	 *            The tolerance to set
	 */
	public void setTolerance(float tolerance) {
		if (tolerance < 1) {
			tolerance = 1;
		}
		this.tolerance = tolerance;
	}
}
