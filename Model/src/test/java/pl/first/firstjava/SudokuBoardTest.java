package pl.first.firstjava;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SudokuBoardTest {

    private int nmbr = 9;
    private SudokuBoard board = new SudokuBoard(nmbr);
    private SudokuBoard board2 = new SudokuBoard(nmbr);
    private SudokuBoard board3 = board;
    private SudokuField field = new SudokuField();

    public SudokuBoardTest(){
    }

    @Test
    public void getSrnTest(){
        Double srnD = Math.sqrt(nmbr);
        int srn = srnD.intValue();
        assertEquals(srn, board.getSrn());
    }

    @Test
    public void getNmbrTest(){assertEquals(nmbr, board.getNmbr());
    }

    @Test
    public void getRowTest() {
        for (int i = 0; i < nmbr; i++)
            assertNotNull(board.getRow(i));
    }

    @Test
    public void getColumnTest() {
        for (int i = 0; i < nmbr; i++)
            assertNotNull(board.getColumn(i));
    }

    @Test
    public void getBoxTest() {
        for (int i = 0; i < nmbr; i += 3) {
            for (int j = 0; j < nmbr; j += 3) {
                assertNotNull(board.getBox(i, j));
            }
        }
    }

    @Test
    public void getPositionTest() {
        assertNotNull(board.getPosition(0,0));
    }

    @Test
    public void setPositionTest() {
        Random r = new Random();
        int x = r.nextInt(9) + 0;
        board.setPosition(0,0, x);
        assertEquals(board.getPosition(0,0), x);

    }

    @Test
    public void checkBoardTest() {
        assertTrue(board.checkBoard());
        board.setPosition(0,0, 1);
        board.setPosition(1,0, 1);
        assertFalse(board.checkBoard());
    }

    @Test
    public void toStringTest() {
        assertNotNull(board.toString());
    }

    @Test
    public void hashCodeTest() {
        assertNotNull(board.hashCode());
        assertNotEquals(board.hashCode(), board2.hashCode());

        assertEquals(board.hashCode(), board3.hashCode());
        board.setPosition(0, 0, 0);
        board.setPosition(0, 1, 0);
        assertEquals(board.hashCode(), board3.hashCode());
    }

    @Test
    public void equalsTest() {
        assertTrue(board.equals(board3)
                &&(board3.equals(board)));

            assertFalse(board.equals(board2)
                    && (board2.equals(board)));

        board.setPosition(0,0,0);
        board.setPosition(0,1,0);
        assertTrue(board.equals(board3)
                &&(board3.equals(board)));

        assertFalse(board.equals(field) && field.equals(board));
    }

    @Test
    public void cloneTest() throws CloneNotSupportedException {
        SudokuBoard deepClone = (SudokuBoard) board.clone();
        assertEquals(board.getBoard(), deepClone.getBoard());

        board.setPosition(0,0,0);
        board.setPosition(0,1,0);

        assertNotEquals(board.getBoard(), deepClone.getBoard());
    }
}
