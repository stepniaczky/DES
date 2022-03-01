package pl.first.firstjava;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class JdbcSudokuBoardDaoTest {

    @Test
    void jdbcTest() throws SQLException {
        SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
        String fileName = "cokolwiek"; // w bazie jest juz plansza o takiej nazwie
        String generatedFileName = RandomStringUtils.randomAlphabetic(10);
        try(Dao<SudokuBoard> file = dao.getJdbcDao(fileName);
            Dao<SudokuBoard> file2 = dao.getJdbcDao(generatedFileName);
            Dao<SudokuBoard> file3 = dao.getJdbcDao(null)) {
            String beforeError = new JdbcSudokuBoardDao("cokolwiek").showAll();

            SudokuBoard testSudokuWrite = new SudokuBoard(9);
            assertThrows(ExceptionInInitializerError.class, () -> file.write(testSudokuWrite)); // blad z pojawieniem sie
            // okna erroru jest jednoznaczny z bledem wstawienia nowego inserta o takiej samej nazwie
            assertThrows(NoClassDefFoundError.class, () -> file3.write(testSudokuWrite));
            String afterError = new JdbcSudokuBoardDao("cokolwiek").showAll();
            assertEquals(beforeError, afterError);

            assertDoesNotThrow(() -> file2.write(testSudokuWrite));
            SudokuBoard testSudokuRead = file2.read();
            assertEquals(testSudokuWrite.getBoard(), testSudokuRead.getBoard());
            assertNotEquals(testSudokuWrite, testSudokuRead);
            String atEnd = new JdbcSudokuBoardDao("cokolwiek").showAll();
            assertNotEquals(beforeError, atEnd);
        } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
