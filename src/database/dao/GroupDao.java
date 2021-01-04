package database.dao;

import database.DBManager;
import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDao implements IGroupDao {

    private final Connection conn;

    public GroupDao(Connection conn) {
        this.conn = conn;
    }

    /*
     * Esta func retorna um objeto grupo criado
     * com a informação retirada da base de dados
     * cujo id corresponde à variável id passada
     * por parametro
     * */
    @Override
    public Group get(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "select * from tb_group where vc_name = ?"
            );
            st.setString(1, name);
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
     * Esta func retorna uma lista de grupos
     * que contém todos os eventos armazenados
     * na BD.
     * */
    @Override
    public List<Group> getAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Group> groups = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_group"
            );
            rs = st.executeQuery();

            while(rs.next()) {
                groups.add(build(rs));
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
     * Esta func recebe toda a informação necessária
     * para depois inserir um grupo da BD.
     * */
    @Override
    public void insert(String name) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_group(vc_name) " +
                            "values (?)"
            );
            st.setString(1,name);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    /*
     * Recebendo o nome do grupo, esta função
     * encarrega-se de eliminar o grupo que
     * corresponda a esse nome.
     * */
    @Override
    public void delete(String name) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_group where vc_name = ?"
            );
            st.setString(1, name);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    /*
     * Esta função retorna os todos os users que
     * pertencerem ao grupo cujo nome é passado por
     * argumento.
     * */
    @Override
    public List<User> getMembers(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_group_member where vc_group_name = ?"
            );
            st.setString(1, name);
            rs = st.executeQuery();
            while (rs.next()) {
                users.add(DBManager.getUserDao().get(rs.getString("vc_user_username")));
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
    * Esta função permite retornar todos os users que
    * não pertencem a um determinado grupo
    * */
    @Override
    public List<User> getNonMembers(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                    "select * from tb_user where vc_username not in (select vc_user_username from tb_group_member where vc_group_name = ?)"
            );
            st.setString(1, name);
            rs = st.executeQuery();
            while (rs.next()) {
                users.add(DBManager.getUserDao().get(rs.getString("vc_username")));
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
     * Esta função permite adicionar um user a um
     * grupo. O nome do grupo e o username do user
     * são passados como parâmetro.
     * */
    @Override
    public void addMember(String name, String username) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "insert into tb_group_member(vc_group_name, vc_user_username) values (?,?)"
            );
            st.setString(1, name);
            st.setString(2, username);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    /*
     * Esta função permite remover um user de um
     * grupo. O nome do grupo e o username do user
     * são passados como parâmetro.
     * */
    @Override
    public void removeMember(String name, String username) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "delete from tb_group_member where vc_group_name = ? and vc_user_username = ?"
            );
            st.setString(1, name);
            st.setString(2, username);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeStatement(st);
        }
    }

    /*
     * Esta função serve para criar objetos do tipo
     * grupo a partir da informação recebida da BD.
     * */
    @Override
    public final Group build(ResultSet rs) throws SQLException {
        return Group.make(rs.getString("vc_name"));
    }
}
