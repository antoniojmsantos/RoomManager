package database;

public class RecordNotFound extends Exception {

    public RecordNotFound(String message) {
        super(message);
    }

    public RecordNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}

