package database.dao;

import shared_data.entities.Event;
import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 * A interface IUserDao reúne um conjunto de funções
 * que permitem retirar informações da base de dados
 * relativas à entidade tb_user.
 * Cada função será explicada em comentários situados
 * por cima do corpo das mesmas.
 * */
public interface IUserDao {
    /*
     * Esta func retorna um objeto User criado
     * com a informação retirada da base de dados
     * cujo username corresponde à variável id passada
     * por parametro
     * */
    User get(String username);

    /*
     * Esta func retorna uma lista de users
     * que contém todas as salas armazenados
     * na BD.
     * */
    List<User> getAll();

    /*
     * Esta func recebe toda a informação necessária
     * para depois inserir uma user na BD.
     * ---
     * -1 -> dominio nao existe na bd
     * 0 ->  problemas na bd
     * > 0 -> true correu bem
     * */
    int insert(String username, String name, String password);

    /*
     * Esta função permite alterar o nível de permissões
     * de um user específico.
     * */
    void updatePermissions(String username, boolean permissions);

    /*
     * Recebendo o id da sala, esta função
     * encarrega-se de eliminar a sala que
     * corresponda a esse id.
     * */
    void delete(String username);

    /*
     * Esta função permite autenticar um utilizador
     * */
    boolean authenticate(String username, String password);

    /*
     * Esta função retorna todos os grupos associados
     * a um user específico.
     * */
    List<Group> getGroups(String username);

    /*
     * Esta função permite retornar todos os eventos
     * pendentes associados a um user específico.
     * Para tal, é chamada uma função que retorna
     * eventos consoante o tipo de estado que lhe for
     * passado por parametro.
     * */
    List<Event> getEventsPending(String username);

    /*
     * Esta função permite retornar todos os eventos
     * aceites associados a um user específico.
     * Para tal, é chamada uma função que retorna
     * eventos consoante o tipo de estado que lhe for
     * passado por parametro.
     * */
    List<Event> getEventsAccepted(String username);

    /*
     * Esta função serve para criar objetos do tipo
     * User a partir das informações recebida da BD.
     * */
    User build(ResultSet rs) throws SQLException;
}
