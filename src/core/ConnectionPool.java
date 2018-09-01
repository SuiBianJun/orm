package core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// connection pool

public class ConnectionPool {

	// sotre connection
    static List<Connection> cl = new ArrayList<Connection>();

    // current poolSize
    static int poolSize = 0;

    // min pool length
    static int min = 10;

    // max pool length
    static int max = 30;

    static final ConnectionPool cp = new ConnectionPool();
    
    static{
        for(int i = 0; i < 20; i++){
            poolSize++;
            try {
                cl.add(DBManager.getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public static ConnectionPool getInstance(){
        return cp;
    }

    // singal model
    private ConnectionPool(){}

    // get connection from pool
    public Connection getConnection() {
        while (cl.size() <= min && poolSize < max) {
            try {
                poolSize++;
                cl.add(DBManager.getConnection());

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        Connection conn = cl.get(cl.size() - 1);
        cl.remove(cl.size() - 1);
        //System.out.println("cl --  >" + cl.size());
        return conn;
    }

    // connection used, then close and store to pool again;
    static void closeConnection(Connection conn){
        if(poolSize > max){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else
            cl.add(conn);
    }

}
