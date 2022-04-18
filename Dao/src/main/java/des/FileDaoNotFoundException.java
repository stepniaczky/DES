package des;

public class FileDaoNotFoundException extends FileDaoException {
    public FileDaoNotFoundException() {
        super("fileNotFound");
    }
}

