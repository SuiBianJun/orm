package core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnection {

    static Connection conn;


    static class Handler{
        final static SingleConnection sc = new SingleConnection();
        static {
            try {
                conn = DBManager.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private SingleConnection(){}

    static SingleConnection getInstance(){

        return Handler.sc;
    }

    public Connection getConnection(){

        return conn;
    }
}
