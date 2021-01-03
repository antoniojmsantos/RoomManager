package database.dao;

import shared_data.entities.Room;
import shared_data.entities.RoomFeature;
import shared_data.entities.RoomType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/*
 * A interface IRoomDao reúne um conjunto de funções
 * que permitem retirar informações da base de dados
 * relativas à entidade tb_room.
 * Cada função será explicada em comentários situados
 * por cima do corpo das mesmas.
 * */
public interface IRoomDao {
    /*
     * Esta func retorna um objeto Room criado
     * com a informação retirada da base de dados
     * cujo id corresponde à variável id passada
     * por parametro
     * */
    Room get(int roomId);

    /*
     * Esta func retorna uma lista de salas
     * que contém todas as salas armazenados
     * na BD.
     * */
    List<Room> getAll();

    /*
     * Esta func recebe toda a informação necessária
     * para depois inserir uma sala na BD.
     * */
    void insert(String name, int capacity, RoomType type, List<RoomFeature> features);

    /*
     * Recebendo o id da sala, esta função
     * encarrega-se de eliminar a sala que
     * corresponda a esse id.
     * */
    void delete(int roomId);

    /*
     * Esta função permite alterar o nome de
     * uma sala específica.
     * */
    void updateName(int roomId, String name);

    /*
     * Esta função permite alterar a capacidade de
     * uma sala específica.
     * */
    void updateCapacity(int roomId, int capacity);

    /*
     * Esta função devolve todas as características
     * associadas a uma sala específica.
     * */
    List<RoomFeature> getFeatures(int roomId);

    /*
     * Esta função permite adicionar uma característica
     * a uma sala específica.
     * */
    void insertFeature(int roomId, RoomFeature feature);

    /*
     * Esta função permite retirar uma característica
     * a uma sala específica.
     * */
    void deleteFeature(int roomId, RoomFeature feature);

    /*
     * Esta função serve para criar objetos do tipo
     * Room a partir das informações recebida da BD.
     * */
    Room build(ResultSet rs) throws SQLException;
}
