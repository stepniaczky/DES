package pl.first.firstjava;

public class FileWriteDaoException extends FileDaoException {
    public FileWriteDaoException() {
        super("writingGoesWrong");
    }
}
