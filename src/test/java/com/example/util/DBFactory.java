package com.example.util;

import javax.enterprise.inject.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.util.DB.JdbcConfig;

public class DBFactory {

	private static final Logger logger = LoggerFactory.getLogger(DBFactory.class);

	@Produces
	public DB createDB() {
		logger.debug("createDB: ");

		JdbcConfig jdbcConfig = new JdbcConfig();
		jdbcConfig.setJdbcUrl("jdbc:sqlite::memory:");

		DB db = new DB(jdbcConfig);

		return db;
	}
}
