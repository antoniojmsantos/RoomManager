package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class RequestCreateEvent extends Request implements Serializable {
    private int idRoom;
    private int duration;
    private String name,nameGroup, usernameCreator;
    private LocalDateTime initialDate;

    public RequestCreateEvent(int idRoom, String nameGroup, String usernameCreator, int duration, String name, LocalDateTime initialDate) {
        this.idRoom = idRoom;
        this.nameGroup = nameGroup;
        this.usernameCreator = usernameCreator;
        this.duration = duration;
        this.name = name;
        this.initialDate = initialDate;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public int getDuration() {
        return duration;
    }

    public String getUsernameCreator() {
        return usernameCreator;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getInitialDate() {
        return initialDate;
    }
}
