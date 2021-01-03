package shared_data.helper;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TimePeriod implements Serializable {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public TimePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public final LocalDateTime start() {
        return startDate;
    }

    public final LocalDateTime end() {
        return endDate;
    }

    public boolean intersects(LocalDateTime date) {
        return date.isAfter(startDate) && date.isBefore(endDate);
    }

    public static TimePeriod make(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isBefore(endDate) && endDate.isAfter(startDate) && startDate.isAfter(LocalDateTime.now())) {
            return new TimePeriod(startDate, endDate);
        }
        throw new IllegalArgumentException();
    }

    public static TimePeriod make(LocalDateTime startDate, int durationInMinutes) {
        LocalDateTime endDate = startDate.plusMinutes(durationInMinutes);
        if (startDate.isBefore(endDate) && endDate.isAfter(startDate) && startDate.isAfter(LocalDateTime.now())) {
            return new TimePeriod(startDate, endDate);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return startDate.toString() +" - "+endDate.toString();
    }
}
