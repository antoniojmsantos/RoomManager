package database.dao;

import database.DBManager;
import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {

    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /*
     * Esta func retorna um objeto User criado
     * com a informação retirada da base de dados
     * cujo username corresponde à variável id passada
     * por parametro
     * */
    @Override
    public User get(String username) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select * from tb_user where vc_username = ?"
            );
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs.next()) {
                return build(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return null;
    }

    /*
     * Esta func retorna uma lista de users
     * que contém todas as salas armazenados
     * na BD.
     * */
    @Override
    public List<User> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_user"
            );
            rs = st.executeQuery();
            while(rs.next()) {
                users.add(build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return users;
    }

    /*
     * Esta func recebe toda a informação necessária
     * para depois inserir uma user na BD.
     * ---
     * -1 -> dominio nao existe na bd
     * 0 ->  problemas na bd
     * > 0 -> true correu bem
     * */
    @Override
    public int insert(String username, String name, String password) {
        PreparedStatement st = null;
        ResultSet rs = null;

        int rows = 0;

        try {

            // get permissions from database
            st = conn.prepareStatement(
                    "select b_has_permissions from tb_account where vc_domain like ?"
            );
            st.setString(1, "@".concat(username.split("@")[1]));
            rs = st.executeQuery();
            if (!rs.next()) {
                return -1;
            }

            // insert into
            st = conn.prepareStatement(
                    "insert into tb_user(vc_username, vc_name, vc_password, b_permissions) " +
                            "values (?,?,md5(?),?)"
            );
            st.setString(1,username);
            st.setString(2,name);
            st.setString(3,password);
            st.setBoolean(4,rs.getBoolean("b_has_permissions"));
            rows = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }
        return rows;
    }

    /*
     * Esta função permite alterar o nível de permissões
     * de um user específico.
     * */
    @Override
    public void updatePermissions(String username, boolean permissions) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "update tb_user set b_permissions = ? where vc_username = ?"
            );
            st.setBoolean(1, permissions);
            st.setString(2, username);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    /*
     * Recebendo o id da sala, esta função
     * encarrega-se de eliminar a sala que
     * corresponda a esse id.
     * */
    @Override
    public void delete(String username) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_user where vc_username = ?"
            );
            st.setString(1, username);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    /*
     * Esta função permite autenticar um utilizador
     * */
    @Override
    public boolean authenticate(String username, String password) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select exists(select * from tb_user where  vc_username = ? and vc_password = md5(?)) as res;"
            );
            st.setString(1, username);
            st.setString(2, password);
            rs = st.executeQuery();

            if (rs.next()) {
                // todo: could be getInt instead
                return rs.getBoolean("res");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(st);
        }
        return false;
    }

    /*
     * Esta função retorna todos os grupos associados
     * a um user específico.
     * */
    @Override
    public List<Group> getGroups(String username) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Group> groups = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_group_member where vc_user_username = ?"
            );
            st.setString(1, username);
            rs = st.executeQuery();
            while (rs.next()) {
                groups.add(DBManager.getGroupDao().build(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }

        return groups;
    }

    /*
     * Esta função permite retornar todos os eventos
     * pendentes associados a um user específico.
     * Para tal, é chamada uma função que retorna
     * eventos consoante o tipo de estado que lhe for
     * passado por parametro.
     * */
    @Override
    public List<Event> getEventsPending(String username) {
        return getEvents(username, "pending");
    }

    /*
     * Esta função permite retornar todos os eventos
     * aceites associados a um user específico.
     * Para tal, é chamada uma função que retorna
     * eventos consoante o tipo de estado que lhe for
     * passado por parametro.
     * */
    @Override
    public List<Event> getEventsAccepted(String username) {
        return getEvents(username, "accepted");
    }

    /*
     * Esta função retorna os eventos de um user
     * consoante o estado que lhe tenha sido
     * passado como parametro.
     * */
    public List<Event> getEvents(String username, String state) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Event> events = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select i_event_id from tb_event_subscription where vc_user_username = ? and vc_state = ?"
            );
            st.setString(1, username);
            st.setString(2, state);
            rs = st.executeQuery();
            while (rs.next()) {
                events.add(DBManager.getEventDao().get(rs.getInt("i_event_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
            DBManager.closeResultSet(rs);
        }

        return events;
    }

    /*
     * Esta função serve para criar objetos do tipo
     * User a partir das informações recebida da BD.
     * */
    @Override
    public final User build(ResultSet rs) throws SQLException {
        return User.make(
                rs.getString("vc_username"),
                rs.getString("vc_name"),
                rs.getString("vc_password"),
                rs.getBoolean("b_permissions")
        );
    }
}
