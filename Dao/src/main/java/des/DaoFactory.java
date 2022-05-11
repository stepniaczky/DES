package des;

public class DaoFactory {
    public Dao<String> getFileDao(String filename) {
        return new FileDao(filename);
    }
}
