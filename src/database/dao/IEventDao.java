package database.dao;

import shared_data.entities.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * A interface IEventDao reúne um conjunto de funções
 * que permitem retirar informações da base de dados
 * relativas à entidade tb_event.
 * Cada função será explicada em comentários situados
 * por cima do corpo das mesmas.
 * */
public interface IEventDao {
    /*
     * Esta func retorna uma lista de eventos
     * que contém todos os eventos armazenados
     * na BD.
     * */
    Event get(int id);

    /*
     * Esta func recebe toda a informação necessária
     * para depois inserir um evento da BD.
     * */
    List<Event> getAll();

    /*
     * Esta func recebe toda a informação necessária
     * para depois inserir um evento na BD.
     * */
    int insert(String name, int roomId, String groupName, String creatorUsername, LocalDateTime startDate, int duration);

    /*
     * Recebendo o id do evento, esta função
     * encarrega-se de eliminar o evento que
     * corresponda a esse id.
     * */
    boolean delete(int id);

    /*
     * Esta função permite ao utilizador aceitar um evento
     * a que escolheu pertencer.
     * Recebe o id do evento assim como o id do user (username)
     * e chama uma função específica para atualizar o estado
     * do utilizador em relação ao evento ("accepted")
     * */
    boolean acceptEvent(int eventId, String userUsername);

    /*
     * Esta função permite ao utilizador recusar um evento.
     * Recebe o id do evento assim como o id do user (username)
     * e chama uma função específica para atualizar o estado
     * do utilizador em relação ao evento ("refused")
     * */
    boolean refuseEvent(int eventId, String userUsername);

    /*
     * Esta função permite ao docente cancelar um evento.
     * Recebe o id do evento assim como o id do user (username)
     * e chama uma função específica para atualizar o estado
     * do utilizador em relação ao evento ("cancelled")
     * */
    public boolean cancelEvent(int eventId, String userUsername);


    /*
     * Esta função retorna todos os eventos que estão associados
     * à sala cujo id é dado por argumento.
     * */
    List<Event> getEventsInRoom(int id_room);

    /*
     * Esta função retorna todos os eventos que foram criados
     * pelo user cujo id foi passado como argumento.
     * */
    List<Event> getEventsByCreator(String username);

    /*
     * Esta função serve para criar objetos do tipo
     * evento a partir da informação recebida da BD.
     * */
    Event build(ResultSet rs) throws SQLException;
}
