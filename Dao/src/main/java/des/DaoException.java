package des;

public class DaoException extends RuntimeException {
    public DaoException(String messageKey) {
        super(messageKey);
    }

    public DaoException() {
        super("somethingIsNotYes");
    }
}
