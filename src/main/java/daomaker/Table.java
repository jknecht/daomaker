package daomaker;

import java.sql.Types;
import java.util.ArrayList;

public class Table {
	private String name;
	private String schema;
	
	private ArrayList<Column> columns = new ArrayList<Column>();
	private ArrayList<Column> keyColumns = new ArrayList<Column>();
	private ArrayList<Index> indices = new ArrayList<Index>();
	
	private boolean hasLob = false;
	
	/**
	 * @return the hasLob
	 */
	public boolean hasLob() {
		return hasLob;
	}
	public void add(Index index) {
		indices.add(index);
	}
	public ArrayList<Index> getIndices() {
		return indices;
	}

	public String getName() {
		return name;
	}

	public String getSchema() {
		return schema;
	}

	public ArrayList<Column> getKeyColumns() {
		return keyColumns;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}

	public ArrayList<Column> getUpdateColumns() {
		ArrayList<Column> updateColumns = new ArrayList<Column>();
		for (Column column : columns) {
			if (!keyColumns.contains(column)) {
				updateColumns.add(column);
			}
		}
		return updateColumns;
	}

	public ArrayList<Column> getInsertColumns() {
		ArrayList<Column> insertColumns = new ArrayList<Column>();
		for (Column column : columns) {
			if (!column.isAutoIncrement()) {
				insertColumns.add(column);
			}
		}
		return insertColumns;
	}

	public ArrayList<Column> getAutoIncrementColumns() {
		ArrayList<Column> insertColumns = new ArrayList<Column>();
		for (Column column : columns) {
			if (column.isAutoIncrement()) {
				insertColumns.add(column);
			}
		}
		return insertColumns;
	}
	
	public Table(String name, String schema) {
		super();
		this.name = name;
		this.schema = schema;
	}
	
	public void add(Column column) {
		this.columns.add(column);
		if (column.isLob()) {
			this.hasLob = true;
		}
	}

	public void addKey(Column column) {
		this.keyColumns.add(column);
	}

	public String getObjectName() {
		return Util.objectNameify(name);
	}
	
}
