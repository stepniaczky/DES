package pl.first.firstjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldsTest {

    SudokuBoard board = new SudokuBoard(9);
    SudokuFields fields = board.getRow(0);
    SudokuFields fields2 = board.getBox(0, 0);
    SudokuFields fields3 = fields;



    @Test
    public void verifyTest() {
        assertNotNull(fields);
        assertTrue(fields.verify());
        fields.getFields().set(fields.getFields().get(1).getFieldValue(), fields.getFields().get(2));
        assertFalse(fields.verify());
    }

    @Test
    public void toStringTest() {
        assertNotNull(fields.toString());
    }

    @Test
    public void hashCodeTest() {
        assertNotNull(fields.hashCode());
        assertNotEquals(fields.hashCode(), fields2.hashCode());

        assertEquals(fields.hashCode(), fields3.hashCode());
        board.setPosition(0, 0, 2);
        board.setPosition(0, 1, 2);
        assertEquals(fields.hashCode(), fields3.hashCode());
    }

    @Test
    public void equalsTest() {
        assertTrue(fields.equals(fields3)
                &&(fields3.equals(fields)));

        assertFalse(fields.equals(fields2)
                && (fields2.equals(fields)));

        board.setPosition(0, 0, 2);
        board.setPosition(0, 1, 2);
        assertTrue(fields.equals(fields3)
                &&(fields3.equals(fields)));

        assertFalse(board.equals(fields) && fields.equals(board));
    }

    @Test
    public void cloneRowTest() {
        SudokuRow row = board.getRow(0);
        SudokuRow rowClone = (SudokuRow) row.clone();

        assertEquals(row.getFields(), rowClone.getFields());

        board.setPosition(0,0,0);
        board.setPosition(0,1,0);

        assertNotEquals(row.getFields(), rowClone.getFields());
    }

    @Test
    public void cloneColumnTest() {
        SudokuColumn column= board.getColumn(0);
        SudokuColumn columnClone = (SudokuColumn) column.clone();

        assertEquals(column.getFields(), columnClone.getFields());

        board.setPosition(0,0,0);
        board.setPosition(1,0,0);

        assertNotEquals(column.getFields(), columnClone.getFields());
    }

    @Test
    public void cloneBoxTest() {
        SudokuBox box = board.getBox(0,0);
        SudokuBox boxClone = (SudokuBox) box.clone();

        assertEquals(box.getFields(), boxClone.getFields());

        board.setPosition(0,0,0);
        board.setPosition(0,1,0);

        assertNotEquals(box.getFields(), boxClone.getFields());
    }
}
