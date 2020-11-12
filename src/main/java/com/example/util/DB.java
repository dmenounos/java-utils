package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB {

	private static final Logger logger = LoggerFactory.getLogger(DB.class);

	private JdbcConfig jdbcConfig;
	private Connection connection;

	public DB(JdbcConfig jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
	}

	public DB(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Executes an SQL command.
	 */
	public void executeStatement(String sql) {
		logger.debug("execute: sql={}", sql);

		Statement statement = null;

		try {
			statement = connection.createStatement();
			statement.execute(sql);
			statement.close();
		}
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.close(statement);
		}
	}

	/**
	 * Executes an SQL command of type INSERT, UPDATE, DELETE.
	 */
	public int executeUpdate(String sql, Object... params) {
		logger.debug("executeUpdate: sql={}, params={}", sql, params);

		PreparedStatement statement = null;

		try {
			// Create a PreparedStatement with the SQL command.
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// Setup the parameters of the statement.
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}

			// Execute the statement.
			statement.executeUpdate();

			// Retrieve the primary key that was created.
			ResultSet keys = statement.getGeneratedKeys();
			int pk =  keys.next() ? keys.getInt(1) : 0;

			statement.close();
			return pk;
		}
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.close(statement);
		}
	}

	/**
	 * Executes an SQL command of type SELECT.
	 */
	public List<Map<String, Object>> executeSelect(String sql, Object... params) {
		logger.debug("executeSelect: sql={}, params={}", sql, params);

		PreparedStatement statement = null;

		try {
			// Create a PreparedStatement with the SQL command.
			statement = connection.prepareStatement(sql);

			// Setup the parameters of the statement.
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}

			// Execute the statement.
			ResultSet resultSet = statement.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();

			// Convert the ResultSet to List<Map<String, Object>>.
			// The list contains all the records. The map is each record.
			List<Map<String, Object>> results = new ArrayList<>();

			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 0; i < metaData.getColumnCount(); i++) {
					row.put(metaData.getColumnName(i + 1), resultSet.getObject(i + 1));
				}
				results.add(row);
			}

			statement.close();
			return results;
		}
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.close(statement);
		}
	}

	/**
	 * Opens the connection to the database.
	 */
	public void openConnection() {
		logger.debug("openConnection: ");

		try {
			if (connection == null) {
				connection = DriverManager.getConnection(jdbcConfig.getJdbcUrl(), jdbcConfig.getJdbcUsername(), jdbcConfig.getJdbcPassword());
			}
		}
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Closes the connection to the database.
	 */
	public void closeConnection() {
		logger.debug("closeConnection: ");

		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * JDBC connection configuration.
	 */
	public static class JdbcConfig {

		private String jdbcUrl;
		private String jdbcUsername;
		private String jdbcPassword;

		public String getJdbcUrl() {
			return jdbcUrl;
		}

		public void setJdbcUrl(String jdbcUrl) {
			this.jdbcUrl = jdbcUrl;
		}

		public String getJdbcUsername() {
			return jdbcUsername;
		}

		public void setJdbcUsername(String jdbcUsername) {
			this.jdbcUsername = jdbcUsername;
		}

		public String getJdbcPassword() {
			return jdbcPassword;
		}

		public void setJdbcPassword(String jdbcPassword) {
			this.jdbcPassword = jdbcPassword;
		}
	}
}
