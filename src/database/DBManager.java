package database;

import database.dao.*;

import java.sql.*;

public abstract class DBManager {

    private static Connection conn = null;

    // dao's
    private static IUserDao userDao;
    private static IGroupDao groupDao;
    private static IGroupMemberDao groupMemberDao;

    public static void init(String URL, String username, String password) {
        if (!isClosed()) {
            try {
                conn = DriverManager.getConnection(URL, username, password);

                userDao = new UserDao(conn);
                groupDao = new GroupDao(conn);
                groupMemberDao = new GroupMemberDao(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static IUserDao getUserDao() {
        return userDao;
    }

    public static IGroupDao getGroupDao() {
        return groupDao;
    }

    public static IGroupMemberDao getGroupMemberDao() {
        return groupMemberDao;
    }

    private static boolean isClosed() {
        return conn == null;
    }

    public static void closeConnection(Connection conn) {
        if (!isClosed()) {
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
