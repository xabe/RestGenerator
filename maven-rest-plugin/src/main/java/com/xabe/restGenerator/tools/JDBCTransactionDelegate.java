package com.xabe.restGenerator.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Clase que se encarga de realizar la conexion a la BBDD
 * @author Chabir Atrahouch
 *
 */
public class JDBCTransactionDelegate{

	private Connection connection = null;
	private Statement statement;
	private boolean oldStateOfAutoCommit = false;
	private int oldStateOfTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
	
	public JDBCTransactionDelegate(String url,String user,String password,String driverName) throws SQLException, ClassNotFoundException{
		Class.forName(driverName);
		Connection connection = DriverManager.getConnection(url, user, password);
		this.init(connection,Connection.TRANSACTION_READ_COMMITTED);
	}

	public JDBCTransactionDelegate(Connection connection) throws SQLException {
		this.init(connection, Connection.TRANSACTION_READ_COMMITTED);
	}

	private void init(Connection connection, int isolation) throws SQLException {
		this.connection = connection;
		this.oldStateOfTransactionIsolation = this.connection.getTransactionIsolation();
		this.connection.setTransactionIsolation(isolation);
		this.statement = connection.createStatement();
	}

	public void commit() throws SQLException {
		this.connection.commit();
	}

	public void endCommint()  throws SQLException {
		this.statement.close();
		this.connection.setAutoCommit(this.oldStateOfAutoCommit);
		this.connection.setTransactionIsolation(this.oldStateOfTransactionIsolation);
	} 
	
	public void end()  throws SQLException {
		this.statement.close();
		this.connection.close();
	} 

	public void rollback()  throws SQLException{
		this.connection.rollback();
	}

	public void start()  throws SQLException {
		if ( this.connection.getAutoCommit()) 
		{
			this.connection.setAutoCommit(false);
			this.oldStateOfAutoCommit = true;
		}
		this.statement = this.connection.createStatement();
	}

	public Connection getConnection() {
		return this.connection;
	}
	
	public Statement getStatement() {
		return this.statement;
	}

}
