package des;

public class DaoFactory {
    public Dao<SudokuBoard> getFileDao(String filename) {
        return new FileDao(filename);
    }
}
