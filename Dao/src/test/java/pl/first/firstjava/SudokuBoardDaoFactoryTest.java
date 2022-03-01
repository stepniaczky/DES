package pl.first.firstjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SudokuBoardDaoFactoryTest {
    @Test
    public void getFileDaoTest() {
        SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
        assertNotNull(dao.getFileDao("Test.txt"));
    }
}
