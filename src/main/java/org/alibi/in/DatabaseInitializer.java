package org.alibi.in;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseInitializer {

    private static final String PROPERTIES_FILE = "application.properties";

    public static void initialize(Connection connection) throws SQLException {
        Properties properties = new Properties();
        try (InputStream input = DatabaseInitializer.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + PROPERTIES_FILE);
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String changeLogFile = properties.getProperty("liquibase.changeLogFile");

        try {
            // Set default schema to coworking_service
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE SCHEMA IF NOT EXISTS coworking_service");
                statement.execute("SET search_path TO coworking_service");
            }

            // Run Liquibase migrations
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new liquibase.database.jvm.JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during database initialization", e);
        }
    }
}
