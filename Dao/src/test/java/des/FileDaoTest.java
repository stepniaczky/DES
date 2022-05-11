package des;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileDaoTest {
    @Test
    public void overallTest() throws IOException {
        DaoFactory dao = new DaoFactory();
        String s1 = "test";
        String s2;
        Dao<String> file;

        file = dao.getFileDao("whatever.txt");
        file.write(s1);
        s2 = file.read();
        assertEquals(s1, s2);
    }

    @Test
    public void readException() throws IOException {
        int nmbr = 9;
        DaoFactory dao = new DaoFactory();
        Dao<String> file;
        file = dao.getFileDao("whatever.txt");
        file.read();
    }

    @Test
    public void writeException() throws RuntimeException {
        DaoFactory dao = new DaoFactory();
        String s1 = "test";
        Dao<String> file;
        file = dao.getFileDao("whatever.txt");
        file.write(s1);
    }
}
