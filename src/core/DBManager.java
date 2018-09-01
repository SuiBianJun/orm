package core;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    static Properties properties;
    private String path;

    // assign db.properties file adrress
    public DBManager(String path) {
        this.path = path;
        init();
    }

    // get connection info from properties
    public static Properties getConf(){

        return properties;
    }

    // get connection info
    public void init(){
        properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(new File(path))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // config connection
    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {

    	properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(new File("db.properties"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
        Class.forName(properties.getProperty("driverName"));
        Connection conn = DriverManager.getConnection(properties.getProperty("databaseServerName") + 
        		properties.getProperty("databaseName"),
        		properties.getProperty("userName"), properties.getProperty("password"));

        return conn;
    }
}
