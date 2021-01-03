package shared_data.entities;

/*
 * A enum RoomType contém várias constantes,
 * cada uma corresponde a um tipo
 * que poderá será atribuído a um quarto.
 * */
public enum RoomType {
    ANFITEATRO("Anfiteatro"),
    AUDITORIO("Auditório"),
    LABORATORIO("Laboratório"),
    SALA_REUNIOES("Sala de Reuniões");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RoomType value(String value) {
        for (RoomType obj : values()) {
            if (obj.getValue().equals(value)) return obj;
        }
        throw new IllegalArgumentException();
    }
}
