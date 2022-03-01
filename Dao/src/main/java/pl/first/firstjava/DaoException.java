package pl.first.firstjava;

public class DaoException extends LocalizedRuntimeException {
    public DaoException(String messageKey) {
        super(messageKey);
    }

    public DaoException() {
        super("somethingIsNotYes");
    }
}
