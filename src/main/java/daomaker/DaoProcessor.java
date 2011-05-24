package daomaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class DaoProcessor {
	Connection conn;
	String tableSpec;
	
	public DaoProcessor(Connection conn, String tableSpec) {
		this.conn = conn;
		this.tableSpec = tableSpec;
	}
	
	public void process() throws SQLException, IOException {
		ArrayList<Table> tables = new ArrayList<Table>();
		
		DatabaseMetaData dbMetadata = conn.getMetaData();
		
		ResultSet tablesRs = dbMetadata.getTables(null, null, tableSpec, null);
		while (tablesRs.next()) {
			if (tablesRs.getString("TABLE_TYPE").equals("TABLE")) {
				System.out.println(tablesRs.getString("TABLE_TYPE") + " - " + tablesRs.getString("TABLE_NAME"));
				Table table = new Table(tablesRs.getString("TABLE_NAME"), tablesRs.getString("TABLE_SCHEM"));
				PreparedStatement ps = conn.prepareStatement("select * from " + table.getName());
				ResultSetMetaData rsMetadata = ps.getMetaData();
				ResultSet columnsRs = dbMetadata.getColumns(tablesRs.getString("TABLE_CAT"), tablesRs.getString("TABLE_SCHEM"), tablesRs.getString("TABLE_NAME"), "");
				while (columnsRs.next()) {
					Column column = new Column();
					column.setDataType(columnsRs.getInt("DATA_TYPE"));
					column.setName(columnsRs.getString("COLUMN_NAME"));
					column.setTypeName(columnsRs.getString("TYPE_NAME"));
					column.setRemarks(columnsRs.getString("REMARKS"));
					for (int i = 0; i < rsMetadata.getColumnCount(); i++) {
						String colName = rsMetadata.getColumnName(i + 1);
						if (colName.equalsIgnoreCase(column.getName())) {
							if (rsMetadata.isAutoIncrement(i + 1)) {
								column.setAutoIncrement(true);
								break;
							}
						}
					}
						
					table.add(column);
				}
				columnsRs.close();

				ResultSet pkRs = dbMetadata.getPrimaryKeys(tablesRs.getString("TABLE_CAT"), tablesRs.getString("TABLE_SCHEM"), tablesRs.getString("TABLE_NAME"));
				while (pkRs.next()) {
					String name = pkRs.getString("COLUMN_NAME");
					for (Column c : table.getColumns()) {
						if (c.getName().equals(name)) {
							table.addKey(c);
						}
					}
					
				}
				pkRs.close();

				ResultSet idxRs = dbMetadata.getIndexInfo(tablesRs.getString("TABLE_CAT"), tablesRs.getString("TABLE_SCHEM"), tablesRs.getString("TABLE_NAME"), false, true);
				while (idxRs.next()) {
					String name = idxRs.getString("INDEX_NAME");
					if (name != null) {
						boolean nonUnique = idxRs.getBoolean("NON_UNIQUE");
						String columnName = idxRs.getString("COLUMN_NAME");
						Index index = new Index(name, !nonUnique);
						int i = table.getIndices().indexOf(index);
						if (i > -1) {
							index = table.getIndices().get(i);
						} else {
							table.add(index);
						}
						
						for (Column c : table.getColumns()) {
							if (c.getName().equals(columnName)) {
								index.add(c);
							}
						}
					}
					
				}
				idxRs.close();

				//find the primary key index and mark it
				if (table.getKeyColumns().size() > 0) {
					for (Index idx : table.getIndices()) {
						if (idx.getColumns().size() == table.getKeyColumns().size()) {
							//assume that this is the PK index until proven otherwise...
							idx.setPrimaryKey(true);
							for (Column idxCol : idx.getColumns()) {
								if (!table.getKeyColumns().contains(idxCol)) {
									//here, we have proven otherwise
									idx.setPrimaryKey(false);
								}
							}
						}
					}
				}
				
				tables.add(table);
			}
		}
		tablesRs.close();
		
		
		
		
		for (Table table : tables) {
			
			HashSet<String> types = new HashSet<String>();
			for (Column  c : table.getColumns()) {
				if (!c.getLongPropertyType().startsWith("java.lang")) {
					types.add(c.getLongPropertyType());
				}
			}
			
			//make sure the output directory exists
			File f = new File("output");
			f.mkdirs();
			
			Velocity.init();
			VelocityContext ctx = new VelocityContext();
			ctx.put("table", table);
			ctx.put("types", types);
			{
				Template template = Velocity.getTemplate("dto.tmpl");
				FileWriter fw = new FileWriter("output/" + table.getObjectName() + "DTO.java");
				template.merge(ctx, fw);
				fw.flush();
				fw.close();
			}
			{
				Template template = Velocity.getTemplate("dao.tmpl");
				FileWriter fw = new FileWriter("output/Generated" + table.getObjectName() + "Dao.java");
				template.merge(ctx, fw);
				fw.flush();
				fw.close();
			}
		}
		
		
	}
}
