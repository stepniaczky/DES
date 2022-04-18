package des;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DaoFactoryTest {
    @Test
    public void getFileDaoTest() {
        DaoFactory dao = new DaoFactory();
        assertNotNull(dao.getFileDao("Test.txt"));
    }
}
