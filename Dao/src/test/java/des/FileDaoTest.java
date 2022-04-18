package des;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileDaoTest {
    @Test
    public void overallTest() throws IOException {
        DaoFactory dao = new DaoFactory();
        SudokuBoard b1 = new SudokuBoard(9);
        SudokuBoard b2;
        Dao<SudokuBoard> file;

        file = dao.getFileDao("whatever.txt");
        file.write(b1);
        b2 = file.read();
        assertEquals(b1.getBoard(), b2.getBoard());
    }

    @Test
    public void readException() throws IOException {
        int nmbr = 9;
        DaoFactory dao = new DaoFactory();
        Dao<SudokuBoard> file;
        file = dao.getFileDao("whatever.txt");
        file.read();
    }

    @Test
    public void writeException() throws RuntimeException {
        int nmbr = 9;
        DaoFactory dao = new DaoFactory();
        SudokuBoard b1 = new SudokuBoard(nmbr);
        Dao<SudokuBoard> file;
        file = dao.getFileDao("whatever.txt");
        file.write(b1);
    }
}
