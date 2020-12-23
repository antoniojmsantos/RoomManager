package database;

import database.dao.*;

import java.sql.*;

public abstract class DBManager {

    private static Connection conn;

    // dao's
    private static UserDao userDao;
    private static GroupDao groupDao;
    private static GroupMemberDao groupMemberDao;

    public static void init(String URL, String username, String password) {
        try {
            conn = DriverManager.getConnection(URL, username, password);

            userDao = new UserDaoImpl(conn);
            groupDao = new GroupDaoImpl(conn);
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

    public static GroupDao getGroupDao() {
        return groupDao;
    }

    public static GroupMemberDao getGroupMemberDao() {
        return groupMemberDao;
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
