package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	
	private static Connect instance;
	@SuppressWarnings("unused")
	private Connection connection;
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "govlash_laundry";
	private final String HOST = "localhost:3306";
	private final String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connect getInstance() {
		if(instance == null) {
			instance = new Connect();
		}
		return instance;
	}
	
	public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
