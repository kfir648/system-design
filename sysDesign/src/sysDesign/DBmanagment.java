package sysDesign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;


public class DBmanagment {
	private Statement s;
	private Connection conn;
	private String protocol = "jdbc:derby:";
	private String dbName = "derbyDB";
	public DBmanagment() {
		try {
			conn = DriverManager.getConnection(protocol + dbName
			        + ";create=true");
			conn.setAutoCommit(false);
			s=conn.createStatement();
			s.execute("CREATE TABLE IF NOT EXISTS customer(ID int, name varchar(40))");
			s.execute("CREATE TABLE IF NOT EXISTS table account(ID int, balance int)");
			s.execute("CREATE TABLE IF NOT EXISTS savings(monthlyAmount int, finalDate varchar(8)))");
			s.execute("CREATE TABLE IF NOT EXISTS table loan(amount int, finalDate varchar(8), firstPayment varchar(8)"
					+ "monthlyPay int");
			s.execute("CREATE TABLE IF NOT EXISTS table transaction
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
