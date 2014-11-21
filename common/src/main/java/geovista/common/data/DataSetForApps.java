/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Authors: Xiping Dai and Frank Hardisty
 */

package geovista.common.data;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This class takes a set of Java arrays of type double[], int[], boolean[], or
 * String[], plus optional spatial (or other) data.
 * 
 * This data is passed in as an Object[]. If the Object[] passed is instantiated
 * and named "dataSet", it is expected that: dataSet[0] = String[] where the
 * length of the string array is the number of attributes to follow. These are
 * the attribute names. dataSet[1] = double[], int[], boolean[], or String[]
 * dataSet[2] = double[], int[], boolean[], or String[] dataSet[...] = double[],
 * int[], boolean[], or String[] dataSet[n] = double[], int[], boolean[], or
 * String[] (optional) dataSet[n + 1] = Other data. Possiblities include Shape[]
 * or DataSetForAppsRecord[]. (optional) dataSet[n + ...] = Other data.
 * 
 * Spatial data in the n+1 (or higher) place will be interpreted as follows:
 * java.awt.geom.GeneralPath => SPATIAL_TYPE_POLYGON
 * geovista.common.data.GeneralPathLine => SPATIAL_TYPE_LINE
 * java.awt.geom.Point2D => SPATIAL_TYPE_POINT Then the spatial data type will
 * be correctly set, and the spatial data will then be accessible through the
 * getShapeData() etc. methods. Spatial data in the n+1 (or higher) place that
 * is not of one of those class types will not be correctly handled in the
 * current version.
 * 
 * 
 * 
 * @author Xiping Dai
 * @author Frank Hardisty
 */
public class DataSetForApps {

    public static final int TYPE_NONE = -1;
    public static final int TYPE_NAME = 0;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_INTEGER = 2;
    public static final int TYPE_BOOLEAN = 3;

    public static final int SPATIAL_TYPE_NONE = -1;
    public static final int SPATIAL_TYPE_POINT = 0;
    public static final int SPATIAL_TYPE_LINE = 1;
    public static final int SPATIAL_TYPE_POLYGON = 2;
    public static final int SPATIAL_TYPE_RASTER = 3;
    public static final int SPATIAL_TYPE_GEOMETRY = 4;
    public static final int SPATIAL_TYPE_MULTIPOINT = 5;

    private transient int spatialType = DataSetForApps.SPATIAL_TYPE_NONE;

    private transient Object[] dataObjectOriginal;
    private transient Object[] dataSetNumericAndSpatial;

    private transient Object[] dataSetNumeric;
    private transient Object[] dataSetFull;
    private transient String[] attributeNames;
    private transient String[] attributeNamesNumeric;
    private transient String[] observationNames;
    private transient String[] attributeDescriptions;
    private transient int[] conditionArray = null;
    private transient int numNumericAttributes;
    private transient int numObservations;
    private transient int[] dataType;

    private String dataSourceName;// adding for Jared

    private transient SpatialWeights spatialWeights;

    public Map<String, String>[] aliases;

    public static int NULL_INT_VALUE = Integer.MIN_VALUE;

    final static Logger logger = Logger.getLogger(DataSetForApps.class
	    .getName());

    @Deprecated
    public DataSetForApps() {

    }

    /**
     * Convenience constructor.
     * 
     * @param varNames
     *            String names of variables
     * @param data
     *            Array of arrays, of types double[], int[], or String[]
     * @param geoms
     *            Shapely shapes
     */
    public DataSetForApps(String[] varNames, Object[] data, Shape[] geoms) {

	Object[] originalData = new Object[data.length + 2];
	originalData[0] = varNames;
	for (int i = 0; i < data.length; i++) {
	    originalData[i + 1] = data[i];
	}
	originalData[originalData.length - 1] = geoms;
	dataObjectOriginal = originalData;
	init(dataObjectOriginal);
    }

    public String findInequality(DataSetForApps otherDSA) {

	// first length
	if (otherDSA.getDataObjectOriginal().length != dataObjectOriginal.length) {

	    String message = "dsas differ in length, this = "
		    + dataObjectOriginal.length + ", other = "
		    + otherDSA.getDataObjectOriginal().length;

	    logger.info(message);
	    return message;
	}

	// next compare types
	for (int i = 0; i < dataObjectOriginal.length; i++) {

	    String message;
	    Object thisArray = dataObjectOriginal[i];
	    Object otherArray = otherDSA.dataObjectOriginal[i];
	    if (thisArray.getClass().getName()
		    .equals(otherArray.getClass().getName()) == false) {
		message = "array " + i + " doesn't match, we have "
			+ thisArray.getClass().getName() + " vs "
			+ otherDSA.getClass().getName();
		logger.info(message);
		return message;
	    }

	}

	return "";
    }

    /**
     * This constructor is equivalent to calling setDataObject(data) with the
     * array passed in.
     * 
     * Note: if this DSA is meant to simulate a previously constructed DSA then
     * the listenerList from the prevoius DSA will need to be set after this.
     */

    public DataSetForApps(Object[] data) {

	dataObjectOriginal = data;
	init(dataObjectOriginal);
    }

    public DataSetForApps(String[] varNames, Object[] data, Shape[] geoms,
	    SpatialWeights weights) {

	Object[] originalData = new Object[data.length + 3];
	originalData[0] = varNames;
	for (int i = 0; i < data.length; i++) {
	    originalData[i + 1] = data[i];
	}
	originalData[originalData.length - 1] = geoms;
	originalData[originalData.length - 2] = weights;
	dataObjectOriginal = originalData;
	init(dataObjectOriginal);
    }

    public DataSetForApps(double[] data) {
	String[] varName = { "Data" };

	Object[] theData = { varName, data };
	dataObjectOriginal = theData;
	init(dataObjectOriginal);
    }

    public DataSetForApps appendData(double[] var) {
	return new DataSetForApps(this, "new_data", var);
    }

    public DataSetForApps(DataSetForApps currData, String varName, double[] var) {
	Object[] originalData = currData.getDataObjectOriginal();
	Object[] newData = new Object[originalData.length + 1];
	String[] oldNames = currData.getAttributeNamesOriginal();
	String[] newNames = new String[oldNames.length + 1];

	newNames[0] = varName;
	for (int i = 1; i < newNames.length; i++) {
	    newNames[i] = oldNames[i - 1];

	}

	newData[0] = newNames;
	newData[1] = var;
	for (int i = 2; i < newData.length; i++) {
	    newData[i] = originalData[i - 1];

	}

	dataObjectOriginal = newData;
	init(dataObjectOriginal);
    }

    /**
     * Returns exactly what was passed in to setDataObject(Object[]).
     */
    public Object[] getDataObjectOriginal() {
	return dataObjectOriginal;
    }

    /**
     * Retuns all data, including observationNames String[] data at the end.
     */
    public Object[] getDataSetFull() {
	return dataSetFull;
    }

    public Object[] getNamedArrays() {
	Object[] namedArrays = new Object[attributeNames.length];
	for (int i = 0; i < namedArrays.length; i++) {
	    namedArrays[i] = dataObjectOriginal[i + 1];

	}
	return namedArrays;
    }

    /**
     * Returns the object array with only numerical variables (double[], int[])
     * from the attribute arrays, plus any other attached spatial objects.
     */
    public Object[] getDataSetNumericAndSpatial() {
	return dataSetNumericAndSpatial;
    }

    /**
     * @return the object array with only numerical variables (double[], int[],
     *         and boolean[]) from the attribute arrays
     */
    public Object[] getDataSetNumeric() {
	return dataSetNumeric;
    }

    /**
     * Returns the attribute names for all input arrays.
     */
    public String[] getAttributeNamesOriginal() {
	return attributeNames;
    }

    /**
     * Returns the names of only the numerical variables (double[], int[], and
     * boolean[]) from the attribute arrays.
     * 
     */
    public String[] getAttributeNamesNumeric() {
	return attributeNamesNumeric;
    }

    /**
     * Returns the appropriate array, counting only the numerical variables
     * (double[], int[], and boolean[]).
     * 
     * This first index is zero, the next one, and so on, the last being
     * getNumberNumericAttributes() -1
     * 
     */
    public Object getAttributeNumeric(int numericIndex) {
	// if (numericIndex >= this.numNumericAttributes) {

	// }
	return dataSetNumericAndSpatial[numericIndex + 1]; // skip
	// attribute
	// names
    }

    /**
     * Returns the names of the observations, if any names were attached. The
     * names are determined by the first attribute name which ends in "name",
     * case insenstive.
     * 
     */
    public String[] getObservationNames() {
	return observationNames;
    }

    /**
     * Returns the name of the observations, at observation "obs", if any names
     * were attached. The names are determined by the first attribute name which
     * ends in "name", case insensitive.
     * 
     */

    public String getObservationName(int obs) {
	return observationNames[obs];
    }

    /**
     * Returns a new int[] of the same length as the number of observations.
     * 
     */
    public int[] getConditionArray() {
	conditionArray = new int[numObservations];
	return conditionArray;
    }

    /**
     * Returns the number of numerical variables (double[], int[], and String[])
     */
    public int getNumberNumericAttributes() {
	return numNumericAttributes;
    }

    /**
     * Returns the data type of each attribute array ...??
     */
    public int[] getDataTypeArray() {
	return dataType;
    }

    /**
     * Returns the number of observations in the data set, for which there are
     * attribute names.
     */
    public int getNumObservations() {
	return numObservations;

    }

    /**
     * Returns the type of spatial data in the data set.
     */
    public int getSpatialType() {
	return spatialType;

    }

    /**
     * Returns data after the named arrays. Could be a zero-length array.
     */
    public Object[] getOtherData() {
	int numOtherObjects = dataObjectOriginal.length - attributeNames.length
		- 1;

	Object[] otherData = new Object[numOtherObjects];
	if (otherData.length == 0) {
	    return otherData;
	}

	for (int i = attributeNames.length + 1; i < dataObjectOriginal.length; i++) {
	    otherData[i - attributeNames.length - 1] = dataObjectOriginal[i];// fah
	    // just
	    // added
	    // a -1
	    // here
	}
	return otherData;

    }

    public String[] getAttributeDescriptions() {
	// XXX hack alert on the second part of this condition
	// not a safe set of assumptions
	if (dataObjectOriginal[dataObjectOriginal.length - 1] == null
		|| !(dataObjectOriginal[dataObjectOriginal.length - 1] instanceof String[])) {
	    attributeDescriptions = null;
	} else {
	    attributeDescriptions = (String[]) dataObjectOriginal[dataObjectOriginal.length - 1];
	}
	return attributeDescriptions;
    }

    /**
     * Returns the first instance of Shape[] found in the indicated place, or
     * else null if the spatial data type is not Shape[] or does not exist.
     */
    public Shape[] getShapeData() {

	int i = getShapeDataPlace();
	if (i > 0) {
	    return (Shape[]) dataObjectOriginal[i];
	}
	return null;
    }

    public List<Integer> getNeighbors(int id) {
	return spatialWeights.getNeighborIDs(id);
    }

    public SpatialWeights getSpatialWeights() {
	return spatialWeights;
    }

    @Deprecated
    public void setSpatialWeights(SpatialWeights weights) {
	spatialWeights = weights;
    }

    /**
     * Returns the first instance of a GeneralPath[] found in the data set,
     * searching from last to first, or else null if none exists.
     */
    public GeneralPath[] getGeneralPathData() {
	for (int i = dataObjectOriginal.length - 1; i > -1; i--) {
	    if (dataObjectOriginal[i] instanceof GeneralPath[]) {
		return (GeneralPath[]) dataObjectOriginal[i];
	    }
	}
	return null;
    }

    /**
     * Returns the place of thefirst instance of a Shape[] found in the data
     * set, searching from last to first, or else -1 if none exists.
     */

    public int getShapeDataPlace() {
	for (int i = dataObjectOriginal.length - 1; i > -1; i--) {
	    if (dataObjectOriginal[i] instanceof Shape[]) {
		return i;
	    }
	}
	return -1;

    }

    /**
     * Returns the first instance of a Point2D[] found in the data set,
     * searching from last to first, or else null if none exists.
     */
    public Point2D[] getPoint2DData() {
	for (int i = dataObjectOriginal.length - 1; i > -1; i--) {
	    if (dataObjectOriginal[i] instanceof Point2D[]) {
		return (Point2D[]) dataObjectOriginal[i];
	    }
	}
	return null;
    }

    /**
     * Returns the first instance of a Geometry[] found in the data set,
     * searching from last to first, or else null if none exists.
     */
    public Geometry[] getGeomData() {
	for (int i = dataObjectOriginal.length - 1; i > -1; i--) {
	    if (dataObjectOriginal[i] instanceof Geometry[]) {
		return (Geometry[]) dataObjectOriginal[i];
	    }
	}
	return null;
    }

    // /**
    // * Returns the first instance of a ShapeFile found in the data set,
    // * searching from last to first, or else null if none exists.
    // */
    // public ShapeFile getShapeFileData(){
    // for (int i = this.dataObjectOriginal.length - 1; i > -1; i--) {
    // if(this.dataObjectOriginal[i] instanceof ShapeFile) {
    // return (ShapeFile)this.dataObjectOriginal[i];
    // }
    // }
    // return null;
    // }

    public int[] getIntArrayDataByName(String name) {
	int[] intData = new int[0];

	for (int i = 0; i < attributeNames.length; i++) {
	    if (name.equals(attributeNames[i])) {
		if (dataObjectOriginal[i + 1] instanceof int[]) { // +1 to
		    // skip
		    // names
		    intData = (int[]) dataObjectOriginal[i + 1];
		}
	    }

	}

	return intData;

    }

    public String[] getStringArrayDataByName(String name) {
	String[] stringData = new String[0];

	for (int i = 0; i < attributeNames.length; i++) {
	    if (name.equals(attributeNames[i])) {
		if (dataObjectOriginal[i + 1] instanceof String[]) {
		    // +1 to
		    // skip
		    // names
		    stringData = (String[]) dataObjectOriginal[i + 1];
		}
	    }

	}

	return stringData;

    }

    /**
     * 
     * 
     * This first index is zero, the next one, and so on, the last being
     * getNumberNumericAttributes() -1
     */
    public double[] getNumericDataAsDouble(int numericArrayIndex) {
	Object dataNumeric = dataSetNumericAndSpatial[numericArrayIndex + 1];
	// because it is a string array of variable names
	double[] doubleData = null;
	if (dataNumeric instanceof double[]) {
	    doubleData = (double[]) dataNumeric;
	} else if (dataNumeric instanceof int[]) {
	    int[] intData = (int[]) dataNumeric;
	    doubleData = new double[intData.length];
	    for (int i = 0; i < intData.length; i++) {
		if (intData[i] == Integer.MIN_VALUE) {
		    doubleData[i] = Double.NaN;
		} else {
		    doubleData[i] = intData[i];
		}
	    } // next i
	} else {
	    throw new IllegalArgumentException(
		    "Unable to parse values in column " + numericArrayIndex
			    + " as a number");
	}
	return doubleData;
    }

    /**
     * Returns a double where the arrayIndex is the nth array in the data set,
     * and obs is the nth observation in that array.
     * 
     * note: this method skips the variable names array
     */

    public double getValueAsDouble(int arrayIndex, int obs) {
	arrayIndex++;// skip variable names array
	Object dataNumeric = dataObjectOriginal[arrayIndex];
	double[] doubleData = null;
	double doubleVal = Double.NaN;
	if (dataNumeric instanceof double[]) {
	    doubleData = (double[]) dataNumeric;
	    doubleVal = doubleData[obs];
	} else if (dataNumeric instanceof int[]) {
	    int[] intData = (int[]) dataNumeric;
	    doubleVal = intData[obs];
	} else {
	    throw new IllegalArgumentException(
		    "Unable to parse values in column " + arrayIndex
			    + " as a number." + "");
	}
	return doubleVal;
    }

    /**
     * Returns a String where the arrayIndex is the nth array in the data set,
     * and obs is the nth observation in that array.
     * 
     * note: this method skips the variable names array
     */

    public String getValueAsString(int arrayIndex, int obs) {
	arrayIndex++;// skip variable names array
	Object dataNumeric = dataObjectOriginal[arrayIndex];
	String stringVal = null;
	if (dataNumeric instanceof double[]) {
	    double[] doubleData = (double[]) dataNumeric;
	    stringVal = String.valueOf(doubleData[obs]);
	} else if (dataNumeric instanceof int[]) {
	    int[] intData = (int[]) dataNumeric;
	    stringVal = String.valueOf(intData[obs]);
	} else if (dataNumeric instanceof String[]) {
	    String[] stringData = (String[]) dataNumeric;
	    stringVal = stringData[obs];
	} else {

	    throw new IllegalArgumentException(
		    "Unable to parse values in column " + arrayIndex
			    + " as a string");
	}
	return stringVal;
    }

    /**
     * Returns a double where the numericArrayIndex is the nth numeric array in
     * the data set, and obs is the nth observation in that array.
     */

    public double getNumericValueAsDouble(int numericArrayIndex, int obs) {
	Object dataNumeric = dataSetNumericAndSpatial[numericArrayIndex + 1]; // we
	// skip
	// the
	// first
	// one
	double[] doubleData = null;
	double doubleVal = Double.NaN;
	if (dataNumeric instanceof double[]) {
	    doubleData = (double[]) dataNumeric;
	    doubleVal = doubleData[obs];
	} else if (dataNumeric instanceof int[]) {
	    int[] intData = (int[]) dataNumeric;
	    doubleVal = intData[obs];
	} else {
	    throw new IllegalArgumentException(
		    "Unable to parse values in column " + numericArrayIndex
			    + " as a number");
	}
	return doubleVal;
    }

    /**
     * Returns the name of the nth numeric array.
     */

    public String getNumericArrayName(int arrayPlace) {
	String[] names = (String[]) dataSetNumericAndSpatial[0];
	return names[arrayPlace];
    }

    /**
     * All initialization work should be done here.
     * 
     * This method accepts the input Object[] and intializes all member
     * variables. In general, member variables do not change after
     * initialization. Before this method is called, all member variables are
     * null.
     * 
     * Any spatial data passed in is assumed to be polygonal if the spatial data
     * is of type Shape[], and assumed to be point if the spatial data is of
     * type Point2D[]. For line types, setDataObject(Object[] data, int
     * spatialType) should be used, or setSpatialType(int spatialType).
     */

    private void init(Object[] data) {
	if (data == null) {
	    return;
	}
	if (!(data[0] instanceof String[])) {
	    throw new IllegalArgumentException(
		    "Data sets passed to DataSetForApps "
			    + "must begin with String[], with the "
			    + "length of the array equal to the "
			    + "number of attribute arrays that follow");
	}

	attributeNames = (String[]) data[0];
	aliases = new Map[attributeNames.length];

	dataSetFull = new Object[dataObjectOriginal.length + 1]; // plus
	// one
	// for
	// the
	// attribute
	// names
	// place
	dataSetFull[0] = attributeNames;

	int len = attributeNames.length;
	dataType = new int[len];
	numNumericAttributes = 0;
	for (int i = 0; i < len; i++) {
	    if (data[i] instanceof SpatialWeights) {
		spatialWeights = (SpatialWeights) data[i];
	    }
	    if (data[i + 1] instanceof String[]) {
		String attrName = attributeNames[i].toLowerCase();
		if (attrName.endsWith("name")) {
		    dataType[i] = DataSetForApps.TYPE_NAME;
		    observationNames = (String[]) data[i + 1];
		}
	    } else if (data[i + 1] instanceof double[]) {
		dataType[i] = DataSetForApps.TYPE_DOUBLE;
		numNumericAttributes++;
	    } else if (data[i + 1] instanceof int[]) {
		dataType[i] = DataSetForApps.TYPE_INTEGER;
		numNumericAttributes++;
	    } else if (data[i + 1] instanceof boolean[]) {
		dataType[i] = DataSetForApps.TYPE_BOOLEAN;
		numNumericAttributes++;
	    } else {
		dataType[i] = DataSetForApps.TYPE_NONE;
	    }

	    dataSetFull[i + 1] = data[i + 1];
	}

	for (Object element : data) {

	    if (element instanceof Shape[]) {
		Shape[] temp = ((Shape[]) element);

		if (temp[0] instanceof GeneralPathLine) {
		    spatialType = DataSetForApps.SPATIAL_TYPE_LINE;
		} else {
		    spatialType = DataSetForApps.SPATIAL_TYPE_POLYGON; // the
		    // default
		    break;
		}
	    } else if (element instanceof Point2D[]) {
		spatialType = DataSetForApps.SPATIAL_TYPE_POINT;

		break;
	    } else if (element instanceof Geometry[]) {
		spatialType = DataSetForApps.SPATIAL_TYPE_GEOMETRY;
		break;
	    } else if (element instanceof List<?>) {
		spatialType = DataSetForApps.SPATIAL_TYPE_MULTIPOINT;
		break;
	    }

	}
	for (Object element : data) {
	    if (element instanceof SpatialWeights) {
		spatialWeights = (SpatialWeights) element;
	    }
	}
	int otherInfo = data.length - 1 - len; // Info objects are arrays
	// besides attribute object,
	// data objects and observ name
	// object.
	dataSetNumericAndSpatial = new Object[numNumericAttributes + 2
		+ otherInfo];
	dataSetNumeric = new Object[numNumericAttributes];
	if (otherInfo > 0) {
	    for (int i = 0; i < otherInfo; i++) {
		dataSetNumericAndSpatial[numNumericAttributes + 2 + i] = data[len
			+ 1 + i];
		dataSetFull[len + 2 + i] = data[len + 1 + i];
	    }
	}
	attributeNamesNumeric = new String[numNumericAttributes];
	int dataTypeIndex = 0;
	for (int i = 0; i < numNumericAttributes; i++) {
	    while ((dataType[dataTypeIndex]) < 1) {
		dataTypeIndex++;
	    }
	    dataSetNumericAndSpatial[i + 1] = data[dataTypeIndex + 1];
	    dataSetNumeric[i] = data[dataTypeIndex + 1];
	    attributeNamesNumeric[i] = attributeNames[dataTypeIndex];

	    dataTypeIndex++;
	}
	// The first object in dataObject array is attribute names.
	dataSetNumericAndSpatial[0] = attributeNamesNumeric;
	// Reserve an object in array for obervation names. The position is
	// after
	// all other data, including spatial data.
	if (observationNames != null) {
	    dataSetNumericAndSpatial[numNumericAttributes + 1] = observationNames;
	    dataSetFull[len + 1] = observationNames;
	} else {
	    dataSetNumericAndSpatial[numNumericAttributes + 1] = null;
	    dataSetFull[len + 1] = observationNames;
	}

	// set the number of observations
	if (dataType[0] == DataSetForApps.TYPE_NAME) {
	    numObservations = ((String[]) dataObjectOriginal[1]).length;
	} else if (dataType[0] == DataSetForApps.TYPE_DOUBLE) {
	    numObservations = ((double[]) dataSetNumericAndSpatial[1]).length;
	} else if (dataType[0] == DataSetForApps.TYPE_INTEGER) {
	    numObservations = ((int[]) dataSetNumericAndSpatial[1]).length;
	} else if (dataType[0] == DataSetForApps.TYPE_BOOLEAN) {
	    numObservations = ((boolean[]) dataSetNumericAndSpatial[1]).length;
	}

    }

    public DataSetForApps appendDataSet(DataSetForApps newData) {
	DataSetForApps returnDataSetForApps = null;
	if (dataObjectOriginal == null) {
	    returnDataSetForApps = new DataSetForApps();
	    returnDataSetForApps.init(newData.getDataObjectOriginal());
	    return returnDataSetForApps;
	}
	String[] newNames = newData.getAttributeNamesOriginal();
	String[] oldNames = getAttributeNamesOriginal();
	String[] concatNames = new String[newNames.length + oldNames.length];
	// get the names
	for (int i = 0; i < oldNames.length; i++) {
	    concatNames[i] = oldNames[i];

	}
	for (int i = oldNames.length; i < concatNames.length; i++) {
	    concatNames[i] = newNames[i - oldNames.length];
	}
	// get the named arrays
	Object[] newObjects = newData.getNamedArrays();
	Object[] oldObjects = getNamedArrays();
	Object[] concatObjects = new Object[newObjects.length
		+ oldObjects.length];
	for (int i = 0; i < oldObjects.length; i++) {
	    concatObjects[i] = oldObjects[i];

	}
	for (int i = oldObjects.length; i < concatObjects.length; i++) {
	    concatObjects[i] = newObjects[i - oldObjects.length];
	}
	// get the "other" objects (spatial etc.)

	Object[] newOtherObjects = newData.getOtherData();

	Object[] oldOtherObjects = getOtherData();
	Object[] concatOtherObjects = new Object[newOtherObjects.length
		+ oldOtherObjects.length];
	for (int i = 0; i < oldOtherObjects.length; i++) {
	    concatOtherObjects[i] = oldOtherObjects[i];

	}
	for (int i = oldOtherObjects.length; i < concatOtherObjects.length; i++) {
	    concatOtherObjects[i] = newOtherObjects[i - oldOtherObjects.length];
	}

	// full up a return array
	Object[] returnArray = new Object[1 + concatObjects.length
		+ concatOtherObjects.length];

	returnArray[0] = concatNames;
	for (int i = 1; i < concatObjects.length + 1; i++) {
	    returnArray[i] = concatObjects[i - 1];
	}
	for (int i = 1 + concatObjects.length; i < returnArray.length; i++) {
	    returnArray[i] = concatOtherObjects[i - 1 - concatObjects.length];
	}

	returnDataSetForApps = new DataSetForApps(returnArray);
	return returnDataSetForApps;

    }

    public DataSetForApps prependDataSet(DataSetForApps newData) {
	DataSetForApps returnDataSetForApps = null;
	if (dataObjectOriginal == null) {
	    returnDataSetForApps = new DataSetForApps();
	    returnDataSetForApps.init(newData.getDataObjectOriginal());
	    return returnDataSetForApps;
	}
	String[] newNames = newData.getAttributeNamesOriginal();
	String[] oldNames = getAttributeNamesOriginal();
	String[] concatNames = new String[newNames.length + oldNames.length];
	// get the names
	for (int i = 0; i < newNames.length; i++) {
	    concatNames[i] = newNames[i];

	}
	for (int i = newNames.length; i < concatNames.length; i++) {
	    concatNames[i] = oldNames[i - newNames.length];
	}
	// get the named arrays
	Object[] newObjects = newData.getNamedArrays();
	Object[] oldObjects = getNamedArrays();
	Object[] concatObjects = new Object[newObjects.length
		+ oldObjects.length];
	for (int i = 0; i < newObjects.length; i++) {
	    concatObjects[i] = newObjects[i];

	}
	for (int i = newObjects.length; i < concatObjects.length; i++) {
	    concatObjects[i] = oldObjects[i - newObjects.length];
	}
	// get the "other" objects (spatial etc.)

	Object[] newOtherObjects = newData.getOtherData();

	Object[] oldOtherObjects = getOtherData();
	Object[] concatOtherObjects = new Object[newOtherObjects.length
		+ oldOtherObjects.length];
	for (int i = 0; i < newOtherObjects.length; i++) {
	    concatOtherObjects[i] = newOtherObjects[i];

	}
	for (int i = newOtherObjects.length; i < concatOtherObjects.length; i++) {
	    concatOtherObjects[i] = oldOtherObjects[i - newOtherObjects.length];
	}

	// full up a return array
	Object[] returnArray = new Object[1 + concatObjects.length
		+ concatOtherObjects.length];

	returnArray[0] = concatNames;
	for (int i = 1; i < concatObjects.length + 1; i++) {
	    returnArray[i] = concatObjects[i - 1];
	}
	for (int i = 1 + concatObjects.length; i < returnArray.length; i++) {
	    returnArray[i] = concatOtherObjects[i - 1 - concatObjects.length];
	}

	returnDataSetForApps = new DataSetForApps(returnArray);
	return returnDataSetForApps;

    }

    /***************************************************************************
     * Following methods are added by Diansheng.
     * 
     **************************************************************************/
    // Different from the above simiar method only in: numericArrayIndex -->
    // numericArrayIndex+1
    public double getNumericValueAsDoubleSkipColNames(int numericColumnIndex,
	    int row) {
	Object dataNumeric = dataSetNumericAndSpatial[numericColumnIndex + 1];
	double[] doubleData = null;
	double doubleVal = Double.NaN;
	if (dataNumeric instanceof double[]) {
	    doubleData = (double[]) dataNumeric;
	    doubleVal = doubleData[row];
	} else if (dataNumeric instanceof int[]) {
	    int[] intData = (int[]) dataNumeric;
	    doubleVal = intData[row];
	} else {
	    String temp = ((String[]) dataNumeric)[row];
	    logger.finest("\n" + dataNumeric.getClass() + "=" + temp + ", col="
		    + (numericColumnIndex + 1) + "(d=" + numNumericAttributes
		    + "), row=" + row + "\n");
	    throw new IllegalArgumentException(
		    "Unable to parse values in column " + numericColumnIndex
			    + " as a number");
	}
	return doubleVal;
    }

    public String[] makeUniqueNames(String[] inputNames) {
	String[] outputNames = new String[inputNames.length];
	// let's use an ArrayList
	// this is not super efficient, but I don't expect this to be called
	// very often
	// i.e. it might be best to call this method once per data set, not once
	// per
	// component
	ArrayList nameList = new ArrayList();
	for (int i = 0; i < inputNames.length; i++) {
	    if (nameList.contains(inputNames[i])) {
		outputNames[i] = outputNames[i] + "_2";
	    } else {
		outputNames[i] = inputNames[i];
	    }
	}

	return outputNames;

    }

    @SuppressWarnings("unused")
    public String makeUniqueName(String duplicatedName, ArrayList nameList) {
	return null;
    }

    public DataSetForApps makeNewDataSet(String columnName, double[] columnData) {
	String[] name = new String[1];
	name[0] = columnName;
	Object[] allData = new Object[2];
	allData[0] = name;
	allData[1] = columnData;

	DataSetForApps dataSet = new DataSetForApps(allData);
	DataSetForApps newDataSet = prependDataSet(dataSet);

	return newDataSet;

    }

    public Object getValueAt(int rowIndex, int columnIndex) {

	Object dataArray = getDataObjectOriginal()[columnIndex + 1];
	Object datum = null;
	if (dataArray instanceof double[]) {
	    double[] doubleArray = (double[]) dataArray;
	    datum = new Double(doubleArray[rowIndex]);
	} else if (dataArray instanceof int[]) {
	    int[] intArray = (int[]) dataArray;
	    datum = new Integer(intArray[rowIndex]);
	} else if (dataArray instanceof String[]) {
	    String[] stringArray = (String[]) dataArray;
	    datum = new String(stringArray[rowIndex]);
	} else if (dataArray instanceof boolean[]) {
	    boolean[] booleanArray = (boolean[]) dataArray;
	    datum = new Boolean(booleanArray[rowIndex]);
	} else {
	    logger.severe("datasetforaps, getcolumnclass, hit unknown array type, type = "
		    + dataArray.getClass());
	}

	return datum;

    }

    public String getAlias(int rowIndex, int columnIndex) {
	Map<String, String> colAliases = null;
	if (aliases[columnIndex + 1] == null) {
	    return "";
	}
	String alias = "";
	colAliases = aliases[columnIndex + 1];
	alias = colAliases.get(String.valueOf(rowIndex));
	if (alias == null) {
	    return "";
	}
	return alias;

    }

    public String getDataSourceName() {
	return dataSourceName;
    }

    public int getColumnCount() {
	return attributeNames.length;
    }

    public String getColumnName(int i) {
	return attributeNames[i];
    }

    /*
     * return a column of attributes associated with a particular attribute
     * index getColumnCount() and getColumnName() should refer to similar
     * indices
     */
    public Object getColumnValues(int i) {

	Object attributes = null;

	if (i > -1 && i < attributeNames.length) {
	    // first index in dataObjectOriginal contains the attribute names so
	    // skip this
	    attributes = dataObjectOriginal[i + 1];
	} else {
	    attributes = new Object[0];
	}

	return attributes;
    }

    /*
     * returns the attribute type of a specific column - indices behave similar
     * to getColumnCount(), getColumnName() and getColumnValues()
     */
    public int getColumnType(int i) {

	int type = TYPE_NONE;

	if (i > -1 && i < dataType.length) {
	    type = dataType[i];
	}

	return type;
    }

}
