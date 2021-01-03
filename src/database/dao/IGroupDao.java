package database.dao;

import shared_data.entities.Group;
import shared_data.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 * A interface IGroupDao reúne um conjunto de funções
 * que permitem retirar informações da base de dados.
 * Cada função será explicada em comentários situados
 * por cima do corpo das mesmas.
 * */
public interface IGroupDao {
    /*
     * Esta func retorna um objeto grupo criado
     * com a informação retirada da base de dados
     * cujo id corresponde à variável id passada
     * por parametro
     * */
    Group get(String name);

    /*
     * Esta func retorna uma lista de grupos
     * que contém todos os eventos armazenados
     * na BD.
     * */
    List<Group> getAll();

    /*
     * Esta func recebe toda a informação necessária
     * para depois inserir um grupo da BD.
     * */
    void insert(String name);

    /*
     * Recebendo o nome do grupo, esta função
     * encarrega-se de eliminar o grupo que
     * corresponda a esse nome.
     * */
    void delete(String name);

    /*
     * Esta função retorna os todos os users que
     * pertencerem ao grupo cujo nome é passado por
     * argumento.
     * */
    List<User> getMembers(String name);

    /*
     * Esta função permite retornar todos os users que
     * não pertencem a um determinado grupo
     * */
    List<User> getNonMembers(String name);

    /*
     * Esta função permite adicionar um user a um
     * grupo. O nome do grupo e o username do user
     * são passados como parâmetro.
     * */
    void addMember(String name, String username);

    /*
     * Esta função permite remover um user de um
     * grupo. O nome do grupo e o username do user
     * são passados como parâmetro.
     * */
    void removeMember(String name, String username);

    /*
     * Esta função serve para criar objetos do tipo
     * grupo a partir da informação recebida da BD.
     * */
    Group build(ResultSet rs) throws SQLException;
}
