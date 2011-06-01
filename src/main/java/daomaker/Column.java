package daomaker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;

import javax.swing.text.StyleContext.SmallAttributeSet;

public class Column {
	private String name;
	private int dataType;
	private String typeName;
	private String remarks;
	private boolean autoIncrement;
	private boolean primaryKey;
	
	/**
	 * @return the primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	/**
	 * @return the lob
	 */
	public boolean isLob() {
		return (dataType == Types.BLOB || dataType == Types.CLOB || dataType == Types.NCLOB);
	}
	
	private static HashMap<Integer, Class> types = new HashMap<Integer, Class>();
	static {
		types.put(Types.ARRAY, java.sql.Array.class);
		types.put(Types.BIGINT, BigInteger.class);
		types.put(Types.BINARY, byte[].class);
		types.put(Types.BIT, Integer.class);
		types.put(Types.BIGINT, BigInteger.class);
		types.put(Types.BLOB, byte[].class);
		types.put(Types.BOOLEAN, Boolean.class);
		types.put(Types.CHAR, String.class);
		types.put(Types.CLOB, String.class);
		types.put(Types.DATE, Date.class);
		types.put(Types.DECIMAL, BigDecimal.class);
		types.put(Types.DOUBLE, Double.class);
		types.put(Types.FLOAT, Float.class);
		types.put(Types.INTEGER, Integer.class);
		types.put(Types.JAVA_OBJECT, Object.class);
		types.put(Types.LONGNVARCHAR, String.class);
		types.put(Types.LONGVARBINARY, byte[].class);
		types.put(Types.LONGVARCHAR, String.class);
		types.put(Types.NCHAR, String.class);
		types.put(Types.NCLOB, String.class);
		types.put(Types.NUMERIC, Number.class);
		types.put(Types.NVARCHAR, String.class);
		types.put(Types.SMALLINT, Short.class);
		types.put(Types.TIME, Time.class);
		types.put(Types.TIMESTAMP, Timestamp.class);
		types.put(Types.TINYINT, Short.class);
		types.put(Types.VARBINARY, byte[].class);
		types.put(Types.VARCHAR, String.class);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	
	public String getPropertyName() {
		return Util.propertyNameify(name);
	}

	public String getGetterName() {
		return Util.objectNameify(name);
	}

	public String getShortPropertyType() {
		Class clazz = types.get(dataType);
		return clazz.getSimpleName();
	}

	public String getLongPropertyType() {
		Class clazz = types.get(dataType);
		return clazz.getName();
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (autoIncrement ? 1231 : 1237);
		result = prime * result + dataType;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		if (autoIncrement != other.autoIncrement)
			return false;
		if (dataType != other.dataType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}
	
	
}
