package daomaker;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		Properties properties = new Properties();
		properties.load(new FileInputStream("connection.properties"));
		String driver = properties.getProperty("driver");
		
		Class.forName(driver);
		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		
		String tableSpec = properties.getProperty("tables");
		
		Connection conn = DriverManager.getConnection(url, username, password);
		DaoProcessor processor = new DaoProcessor(conn, tableSpec);
		processor.process();
		conn.close();
	}
}
