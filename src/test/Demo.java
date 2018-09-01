package test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Demo {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        Properties properties = new Properties();
        properties.load(new BufferedReader(new FileReader(new File("db.properties"))));

        Class.forName(properties.getProperty("driverName"));
        
        Connection conn = DriverManager.getConnection(properties.getProperty("databaseServerName") + properties.getProperty("databaseName"),
                properties.getProperty("userName"), properties.getProperty("password"));

    }
}
