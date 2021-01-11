package shared_data.communication.request;

import shared_data.communication.Request;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Request que representa a criação de um evento
 */
public class RequestCreateEvent extends Request implements Serializable {
    private int roomId;
    private int duration;
    private String name, groupName, creatorUsername;
    private LocalDateTime startDate;

    public RequestCreateEvent(String name, int roomId, String groupName, String creatorUsername, LocalDateTime startDate, int duration) {
        this.roomId = roomId;
        this.groupName = groupName;
        this.creatorUsername = creatorUsername;
        this.duration = duration;
        this.name = name;
        this.startDate = startDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getDuration() {
        return duration;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
}
