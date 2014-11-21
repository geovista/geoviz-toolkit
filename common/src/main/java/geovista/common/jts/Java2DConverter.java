/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package geovista.common.jts;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Converts JTS Geometry objects into Java 2D Shape objects
 */
public class Java2DConverter {
    private static double POINT_MARKER_SIZE = 0.0;
    private final AffineTransform pointConverter;

    public Java2DConverter(AffineTransform pointConverter) {
	this.pointConverter = pointConverter;
    }

    private Shape toShape(Polygon p) {
	ArrayList holeVertexCollection = new ArrayList();

	for (int j = 0; j < p.getNumInteriorRing(); j++) {
	    holeVertexCollection.add(toViewCoordinates(p.getInteriorRingN(j)
		    .getCoordinates()));
	}

	return new PolygonShape(toViewCoordinates(p.getExteriorRing()
		.getCoordinates()), holeVertexCollection);
    }

    private Coordinate[] toViewCoordinates(Coordinate[] modelCoordinates) {
	Coordinate[] viewCoordinates = new Coordinate[modelCoordinates.length];

	for (int i = 0; i < modelCoordinates.length; i++) {
	    Point2D point2D = toViewPoint(modelCoordinates[i]);
	    viewCoordinates[i] = new Coordinate(point2D.getX(), point2D.getY());
	}

	return viewCoordinates;
    }

    private Shape toShape(GeometryCollection gc)
	    throws NoninvertibleTransformException {
	GeometryCollectionShape shape = new GeometryCollectionShape();

	for (int i = 0; i < gc.getNumGeometries(); i++) {
	    Geometry g = gc.getGeometryN(i);
	    shape.add(toShape(g));
	}

	return shape;
    }

    private GeneralPath toShape(MultiLineString mls)
	    throws NoninvertibleTransformException {
	GeneralPath path = new GeneralPath();

	for (int i = 0; i < mls.getNumGeometries(); i++) {
	    LineString lineString = (LineString) mls.getGeometryN(i);
	    path.append(toShape(lineString), false);
	}

	// BasicFeatureRenderer expects LineStrings and MultiLineStrings to be
	// converted to GeneralPaths. [Jon Aquino]
	return path;
    }

    // private GeneralPath toShape(LineString lineString)
    // throws NoninvertibleTransformException {
    // GeneralPath shape = new GeneralPath();
    // Point2D viewPoint = toViewPoint(lineString.getCoordinateN(0));
    // shape.moveTo((double) viewPoint.getX(), (double) viewPoint.getY());
    //
    // for (int i = 1; i < lineString.getNumPoints(); i++) {
    // viewPoint = toViewPoint(lineString.getCoordinateN(i));
    // shape.lineTo((double) viewPoint.getX(), (double) viewPoint.getY());
    // }
    //
    // //BasicFeatureRenderer expects LineStrings and MultiLineStrings to be
    // //converted to GeneralPaths. [Jon Aquino]
    // return shape;
    // }

    private Shape toShape(Point point) {
	Rectangle2D.Double pointMarker = new Rectangle2D.Double(0.0, 0.0,
		POINT_MARKER_SIZE, POINT_MARKER_SIZE);

	Point2D viewPoint = toViewPoint(point.getCoordinate());
	pointMarker.x = (viewPoint.getX() - (POINT_MARKER_SIZE / 2));
	pointMarker.y = (viewPoint.getY() - (POINT_MARKER_SIZE / 2));

	return pointMarker;
	// GeneralPath path = new GeneralPath();

	// return viewPoint;
    }

    private Point2D toViewPoint(Coordinate modelCoordinate) {
	// Do the rounding now; don't rely on Java 2D rounding, because it
	// seems to do it differently for drawing and filling, resulting in the
	// draw
	// being a pixel off from the fill sometimes. [Jon Aquino]
	double x = modelCoordinate.x;
	double y = modelCoordinate.y;
	// x = Math.round(x);
	// y = Math.round(y);

	Point2D modelPoint = new Point2D.Double(x, y);

	Point2D viewPoint = pointConverter.transform(modelPoint, null);

	return viewPoint;
    }

    /**
     * If you pass in a general GeometryCollection, note that a Shape cannot
     * preserve information about which elements are 1D and which are 2D. For
     * example, if you pass in a GeometryCollection containing a ring and a
     * disk, you cannot render them as such: if you use Graphics.fill, you'll
     * get two disks, and if you use Graphics.draw, you'll get two rings.
     * Solution: create Shapes for each element.
     */
    public Shape toShape(Geometry geometry)
	    throws NoninvertibleTransformException {
	if (geometry.isEmpty()) {
	    return new GeneralPath();
	}

	if (geometry instanceof Polygon) {
	    return toShape((Polygon) geometry);
	}

	if (geometry instanceof MultiPolygon) {
	    return toShape((MultiPolygon) geometry);
	}

	if (geometry instanceof LineString) {
	    return toShape(geometry);
	}

	if (geometry instanceof MultiLineString) {
	    return toShape((MultiLineString) geometry);
	}

	if (geometry instanceof Point) {
	    return toShape((Point) geometry);
	}
	// as of JTS 1.8, the only thing that this might be is a
	// jts.geom.MultiPoint, because
	// MultiPolygon and MultiLineString are caught above. [Frank Hardisty]
	if (geometry instanceof GeometryCollection) {
	    return toShape((GeometryCollection) geometry);
	}

	throw new IllegalArgumentException("Unrecognized Geometry class: "
		+ geometry.getClass());
    }

    public static Geometry toMultiGon(GeneralPath path) {
	// TODO Auto-generated method stub
	return null;
    }
}
