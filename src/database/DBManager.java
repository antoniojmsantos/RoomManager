package database;

import database.dao.UserDao;
import database.dao.UserDaoImpl;

import java.sql.*;

public abstract class DBManager {

    private static Connection conn;

    // dao's
    private static UserDao userDao;

    public static void init(String URL, String username, String password) {
        try {
            conn = DriverManager.getConnection(URL, username, password);

            userDao = new UserDaoImpl(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static UserDao getUserDao() {
        return userDao;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
