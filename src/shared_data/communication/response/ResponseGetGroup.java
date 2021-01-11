package shared_data.communication.response;

import shared_data.communication.Response;
import shared_data.entities.Group;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Resposta para o pedido de obtenção de todos os grupos
 */
public class ResponseGetGroup extends Response implements Serializable {
    private ArrayList<Group> groups;

    public ResponseGetGroup(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }
}
