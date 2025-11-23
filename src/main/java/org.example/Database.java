package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.example.Secrets.getPassword;
import static org.example.Secrets.getUsername;

public class Database {
    public static Connection connection;

    public static Connection getConnection(){
        if (connection != null) return connection;
        String url = "jdbc:postgresql" + Secrets.getDBName();
        Properties properties = new Properties();
        properties.setProperty("user", getUsername());
        properties.setProperty("password", getPassword());
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
