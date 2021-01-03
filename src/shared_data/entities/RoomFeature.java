package shared_data.entities;

/*
 * A enum RoomFeature contém várias constantes,
 * cada uma corresponde a uma característica
 * que poderá ser atribuída às salas.
 * */
public enum RoomFeature {
    AR_CONDICIONADO("Ar Condicionado"),
    COMPUTADORES_MAC("Computadores MacOS"),
    COMPUTADORES_WINDOWS("Computadores Windows"),
    PROJETOR("Projetor"),
    MESA_REUNIAO("Mesa de Reunião"),
    QUADRO_INTERATIVO("Quadro Interativo");

    private final String value;

    RoomFeature(String value) {
        this.value = value;
    }

    public final String getValue() {
        return value;
    }

    public static RoomFeature value(String value) {
        for (RoomFeature obj : values()) {
            if (obj.getValue().equals(value)) return obj;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return getValue();
    }
}
