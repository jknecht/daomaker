package daomaker;

import java.util.ArrayList;

public class Index {
	private String name;
	private boolean unique;
	private boolean primaryKey;
	private ArrayList<Column> columns = new ArrayList<Column>();
	
	public Index(String name, boolean unique) {
		super();
		this.name = name;
		this.unique = unique;
	}

	public String getName() {
		return name;
	}

	public boolean isUnique() {
		return unique;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}
	
	public void add(Column column) {
		this.columns.add(column);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Index other = (Index) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String getObjectName() {
		return Util.objectNameify(name);
	}

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


}
