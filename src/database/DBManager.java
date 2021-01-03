package database;

import database.dao.*;

import java.sql.*;

/*
 * A classe abstrata DBManager reúne variáveis e funções
 * que permitem estabelecer conexões com a BD.
 * Para tal, são criadas variáveis para cada
 * classe de tipo DAO e uma outra variável
 * do tipo Connection que irá então receber
 * a conexão à BD através da chamada à função
 * getConnection presente na classe DriveManager.
 * Essa conexão serve para que sejam declaradas as
 * variáveis DAO anteriormente inicializadas.
 * Desta forma, também as variáveis DAO terão
 * acesso à BD.
 * Estão também presentes funções get/set para cada variável.
 * Informações relativas a funções específicas encontram-se
 * por cima do protótipo das mesmas.
 * */
public abstract class DBManager {

    private static Connection conn = null;

    private static IUserDao userDao;
    private static IGroupDao groupDao;
    private static IEventDao eventDao;
    private static IRoomDao roomDao;

    public static void init(String URL, String username, String password) {
        try {
                conn = DriverManager.getConnection(URL, username, password);

                userDao = new UserDao(conn);
                groupDao = new GroupDao(conn);
                eventDao = new EventDao(conn);
                roomDao = new RoomDao(conn);
            } catch (SQLException e) {
                e.printStackTrace();
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

    public static IEventDao getEventDao(){
        return eventDao;
    }

    public static IRoomDao getRoomDao(){
        return roomDao;
    }

    //verifica se a conexão foi terminada
    private static boolean isClosed() {
        return conn == null;
    }

    //termina a conexão à BD
    public static void closeConnection(Connection conn) {
        if (!isClosed()) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Fecha um statement passado por parâmetro.
    //liberta a base de dados manualmente do objeto st, em vez de automaticamente
    //questões de boas práticas
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Liberta a base de dados do objeto rs manualmente, em vez de automaticamente
    //questões de boas práticas
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
