package com.example.util;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBTest {

	private static final Logger logger = LoggerFactory.getLogger(DBTest.class);

	private DB db;

	@Before
	public void init() {
		logger.debug("init: ");

		SeContainer container = SeContainerInitializer.newInstance().initialize();
		Instance<DB> dbInstance = container.select(DB.class);
		db = dbInstance.get();
	}

	@Test
	public void test_connection() {
		logger.debug("test_connection: ");

		try {
			db.openConnection();
		}
		finally {
			db.closeConnection();
		}
	}

	@Test
	public void test_statement() {
		logger.debug("test_statement: ");

		try {
			db.openConnection();

			String sql;

			sql = "create table if not exists dbtest ( "
				+ "  id integer primary key autoincrement "
				+ ") ";

			db.executeStatement(sql);
		}
		finally {
			db.closeConnection();
		}
	}
}
