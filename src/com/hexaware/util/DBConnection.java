package com.hexaware.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	static Connection connection;
	public static Connection getDbConnection() {
		String fileName="db.properties";
		Properties properties = new Properties();
		FileInputStream fileInputStream =null;
		
		try {
			fileInputStream = new FileInputStream(fileName);
			properties.load(fileInputStream);
			
			String url = properties.getProperty("db.url");
			String un =properties.getProperty("db.username");
			String pwd =properties.getProperty("db.password");
			System.out.println(url+un+pwd);
		connection=DriverManager.getConnection(url,un,pwd);
		} catch (SQLException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return connection;
	}
	
	public static void main(String[] args) {
		System.out.println(getDbConnection());

	}

}
