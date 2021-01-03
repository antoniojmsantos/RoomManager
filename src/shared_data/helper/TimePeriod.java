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

    public static LocalDateTime periodToDate(LocalDateTime date, int durationInMinutes) {
        return date.plusMinutes(durationInMinutes);
    }

    public static boolean overlaps(
            LocalDateTime fromA,
            LocalDateTime toA,
            LocalDateTime fromB,
            LocalDateTime toB
    ) {
        return isBetween(fromA, toA, fromB) || isBetween(fromA, toA, toB);
    }

    public static boolean isBetween(LocalDateTime from, LocalDateTime to, LocalDateTime candidate) {
        return candidate.isAfter(from) && candidate.isBefore(to);
    }

    public static boolean isBetween(LocalDateTime from, int durationInMinutes, LocalDateTime candidate) {
        return isBetween(from, from.plusMinutes(durationInMinutes), candidate);
    }

    public boolean intersects(LocalDateTime date) {
        return date.isAfter(startDate) && date.isBefore(endDate);
    }

    public static TimePeriod make(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isBefore(endDate) && endDate.isAfter(startDate)) {
            return new TimePeriod(startDate, endDate);
        }
        throw new IllegalArgumentException();
    }

    public static TimePeriod make(LocalDateTime startDate, int durationInMinutes) {
        LocalDateTime endDate = startDate.plusMinutes(durationInMinutes);
        if (startDate.isBefore(endDate) && endDate.isAfter(startDate)) {
            return new TimePeriod(startDate, endDate);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return startDate.toString() +" - "+endDate.toString();
    }
}
