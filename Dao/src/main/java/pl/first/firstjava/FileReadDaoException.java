package pl.first.firstjava;

public class FileReadDaoException extends FileDaoException {
    public FileReadDaoException() {
        super("readingGoesWrong");
    }
}
