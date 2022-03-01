package pl.first.firstjava;

public class FileDaoNotFoundException extends FileDaoException {
    public FileDaoNotFoundException() {
        super("fileNotFound");
    }
}

