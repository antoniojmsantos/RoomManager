package shared_data.helper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimePeriod implements Serializable {

    public static DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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
        return normalize(date).plusMinutes(durationInMinutes);
    }

    public static boolean overlaps(
            LocalDateTime fromA,
            LocalDateTime toA,
            LocalDateTime fromB,
            LocalDateTime toB
    ) {
        return isBetween(fromA, toA, fromB) || isBetween(fromA, toA, toB);
    }

    public static LocalDateTime normalize(LocalDateTime date) {
        return LocalDateTime.parse(date.format(DEFAULT_FORMAT),DEFAULT_FORMAT);
    }

    public static boolean isBetween(LocalDateTime from, LocalDateTime to, LocalDateTime candidate) {
        return normalize(candidate).isAfter(normalize(from)) && normalize(candidate).isBefore(normalize(to));
        //return candidate.isAfter(from) && candidate.isBefore(to);
    }

    public static boolean isBetween(LocalDateTime from, int durationInMinutes, LocalDateTime candidate) {
        return isBetween(from, from.plusMinutes(durationInMinutes), candidate);
    }

    public boolean intersects(LocalDateTime date) {
        return normalize(date).isAfter(normalize(startDate)) && normalize(date).isBefore(normalize(endDate));
        //return date.isAfter(startDate) && date.isBefore(endDate);
    }

    public static TimePeriod make(LocalDateTime startDate, LocalDateTime endDate) {
        if (normalize(startDate).isBefore(normalize(endDate)) && normalize(endDate).isAfter(normalize(startDate))) {
            return new TimePeriod(normalize(startDate), normalize(endDate));
        }
        throw new IllegalArgumentException();
    }

    public static TimePeriod make(LocalDateTime startDate, int durationInMinutes) {
        LocalDateTime endDate = startDate.plusMinutes(durationInMinutes);
        if (normalize(startDate).isBefore(normalize(endDate)) && normalize(endDate).isAfter(normalize(startDate))) {
            return new TimePeriod(normalize(startDate), normalize(endDate));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return startDate.toString() +" - "+endDate.toString();
    }
}
