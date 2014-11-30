/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.symbolization;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class AffineTransformModifier {

	public static AffineTransform makeGeogAffineTransform(Rectangle2D source,
			Rectangle2D target, boolean mirror, boolean flipVertical) {

		AffineTransform xForm = new AffineTransform();
		double sX, sY; // tX, tY for translation, sX, sY for scaling

		// we need to know two things: how much to scale, and how much to
		// translate

		// tY = ((double)this.getHeight()) - (yMax -yMin);
		// scale
		sX = target.getWidth() / (source.getWidth());
		sY = target.getHeight() / (source.getHeight());
		double sFactor = 0;
		if (sX <= sY) {
			sFactor = sX;
		} else {
			sFactor = sY;
		}
		// remember that transforms are applied last to first, so scale then
		// translate
		xForm = new AffineTransform();
		double rotation = Math.toRadians(180);//

		if (flipVertical) {
			xForm.translate(
					(source.getWidth() * sFactor) + (target.getX() * 2),
					(source.getHeight() * sFactor) + (target.getY() * 2)); // fix
			// problems
			// generated
			// by
			// turning
			// upside
			// down
			xForm.rotate(rotation);// turn upside down
		}
		// step 5: if flipVertical, flip then fix

		if (mirror) {
			xForm.translate(
					(source.getWidth() * sFactor) + (target.getX() * 2), 0); // fix
			// problem
			// created
			// by
			// flipping
			// mirror
			// image
			xForm.scale(-1, 1);// flip mirror image
		}
		// step 4: if this is a mirror, flip then fix

		double transX;
		double transY;
		transX = target.getX();
		transY = target.getY();
		// which ever is not the scale factor, we need to move to the middle of
		// that one.
		if (Math.abs(sY - sX) < .0000001) {
			// do nothing, i.e. no nudge factor necassary
		} else if (Math.abs(sFactor - sX) < .0000001) {
			double newHeight = sFactor * source.getHeight();
			double halfTargetHeight = target.getHeight() / 2;
			double halfNewHeight = newHeight / 2;
			int nudgeFactor = (int) (halfTargetHeight - halfNewHeight);
			transY = transY - nudgeFactor;
		} else {
			double newWidth = sFactor * source.getWidth();
			double halfTargetWidth = target.getWidth() / 2;
			double halfNewWidth = newWidth / 2;
			int nudgeFactor = (int) (halfTargetWidth - halfNewWidth);
			transX = transX + nudgeFactor;
		}
		xForm.translate(transX, transY);
		// step 3: move to new location

		xForm.scale(sFactor, sFactor);
		// step 2: scale by lowest factor

		xForm.translate(source.getX() * -1, source.getY() * -1);
		// step 1: move to zero zero
		// workaround to get the map into the top left corner...
		/*
		 * Point2D.Double origin = new Point2D.Double(xMin, yMax);
		 * xForm.transform(origin, origin); if (origin.getY() > 2 +
		 * target.getY()) { AffineTransform xForm2 = new AffineTransform();
		 * xForm2.translate(0,-origin.getY()); xForm.preConcatenate(xForm2); }
		 */

		return xForm;

	}

	public static AffineTransform makeZoomingAffineTransform(
			Rectangle2D source, Rectangle2D target) {
		AffineTransform xForm = new AffineTransform();
		double tX, tY, sX, sY; // tX, tY for translation, sX, sY for scaling

		// we need to know two things: how much to scale, and how much to
		// translate

		// let's translate first.
		tX = target.getX() + (source.getX() * -1);
		tY = target.getY() + (source.getY() * -1);
		// tY = ((double)this.getHeight()) - (yMax -yMin);
		// scale
		sX = target.getWidth() / (source.getWidth());
		sY = target.getHeight() / (source.getHeight());
		double sFactor = 0;
		if (sX <= sY) {
			sFactor = sX;
		} else {
			sFactor = sY;
		}
		// remember that transforms are applied last to first, so scale then
		// translate
		xForm = new AffineTransform();

		double transX = 0;
		double transY = 0;
		// transX = target.getX();// - source.getX();
		// transY = target.getY();// - source.getY();
		// which ever is not the scale factor, we need to move to the middle of
		// that one.
		if (!(Math.abs(sX - sFactor) < .0000001)) {
			double newHeight = sFactor * source.getHeight();
			double halfTargetHeight = target.getHeight() / 2;
			double halfNewHeight = newHeight / 2;
			int nudgeFactor = (int) (halfTargetHeight - halfNewHeight);
			transY = transY + nudgeFactor;
		} else {
			double newWidth = sFactor * source.getWidth();
			double halfTargetWidth = target.getWidth() / 2;
			double halfNewWidth = newWidth / 2;
			int nudgeFactor = (int) (halfTargetWidth - halfNewWidth);
			transX = transX + nudgeFactor;
		}

		xForm.translate(transX, transY);
		// step 5: nudge

		xForm.translate(tX, tY);
		// step 4: move to new location

		xForm.translate(source.getX(), source.getY());
		// step 3: move back to where we were

		xForm.scale(sFactor, sFactor);
		// step 2: scale by lowest factor

		xForm.translate(source.getX() * -1, source.getY() * -1);
		// step 1: move to zero zero
		// workaround to get the map into the top left corner...
		/*
		 * Point2D.Double origin = new Point2D.Double(xMin, yMax);
		 * xForm.transform(origin, origin); if (origin.getY() > 2 +
		 * target.getY()) { AffineTransform xForm2 = new AffineTransform();
		 * xForm2.translate(0,-origin.getY()); xForm.preConcatenate(xForm2); }
		 */

		return xForm;

	}
}
