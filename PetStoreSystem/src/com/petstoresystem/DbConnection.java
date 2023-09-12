package com.petstoresystem;

import java.sql.*;

public class DbConnection {

	 private Connection connection ;
	
	 private final String jdbcURL = "jdbc:oracle:thin:@localhost:1521:orcl"; // Modify with your database URL
	    private final String jdbcUsername = "scott"; // Modify with your database username
	    private final String jdbcPassword = "tiger"; // Modify with your database password
	 
	  public Connection getConnection() {
	        //Connection connection = null;
		  if(connection != null) return connection;
	        try {
	            Class.forName("oracle.jdbc.driver.OracleDriver");
	            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	        }
	        return connection;
	    }
	
}
