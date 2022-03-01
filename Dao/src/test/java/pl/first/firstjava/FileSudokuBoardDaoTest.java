package pl.first.firstjava;

import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class FileSudokuBoardDaoTest {
    @Test
    public void overallTest() throws SQLException {
        SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
        SudokuBoard b1 = new SudokuBoard(9);
        SudokuBoard b2;
        Dao<SudokuBoard> file;

        file = dao.getFileDao("whatever");
        file.write(b1);
        b2 = file.read();
        assertEquals(b1.getBoard(), b2.getBoard());
    }

    @Test
    public void readException() throws LocalizedRuntimeException, SQLException {
        int nmbr = 9;
        SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
        Dao<SudokuBoard> file;
        file = dao.getFileDao("whatever.txt");
        file.read();
    }

    @Test
    public void writeException() throws RuntimeException, SQLException {
        int nmbr = 9;
        SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
        SudokuBoard b1 = new SudokuBoard(nmbr);
        Dao<SudokuBoard> file;
        file = dao.getFileDao("whatever.txt");
        file.write(b1);
    }
}
