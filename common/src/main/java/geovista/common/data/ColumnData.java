package geovista.common.data;

public interface ColumnData {
	public enum DataAttributes {
		IsNumeric, IsCategorical, IsSpatial, IsTemporal
	}

	public String getAlias();

	public String getName();

	public int getNumObservations();

	public double getDoubleVal(int index);

	public int getIntVal(int index);

	public String getStringVal(int index);

}
