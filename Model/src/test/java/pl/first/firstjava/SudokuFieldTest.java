package pl.first.firstjava;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {
    private SudokuField sudokuField = new SudokuField();
    private SudokuField sudokuField2 = new SudokuField(1);
    private SudokuField sudokuField3 = sudokuField;
    private SudokuBoard board = new SudokuBoard(9);

    public SudokuFieldTest() {
    }

    @Test
    public void getFieldValueTest() {
        assertEquals(sudokuField.getFieldValue(), 0);
    }

    @Test
    public void setFieldValueTest() {
        assertEquals(sudokuField.getFieldValue(), 0);
        sudokuField.setFieldValue(1);
        assertEquals(sudokuField.getFieldValue(), 1);
    }

    @Test
    public void toStringTest() {
        assertNotNull(sudokuField.toString());
    }

    @Test
    public void hashCodeTest() {
        assertNotNull(sudokuField.hashCode());
        assertNotEquals(sudokuField.hashCode(), sudokuField2.hashCode());

        assertEquals(sudokuField.hashCode(), sudokuField3.hashCode());
        sudokuField.setFieldValue(2);
        assertEquals(sudokuField.hashCode(), sudokuField3.hashCode());
    }

    @Test
    public void equalsTest() {
        assertTrue(sudokuField.equals(sudokuField3)
                &&(sudokuField3.equals(sudokuField)));

        assertFalse(sudokuField.equals(sudokuField2)
        && (sudokuField2.equals(sudokuField)));

        sudokuField.setFieldValue(2);
        assertTrue(sudokuField.equals(sudokuField3)
                &&(sudokuField3.equals(sudokuField)));

        assertFalse(board.equals(sudokuField) && sudokuField.equals(board));
    }

    @Test
    void cloneTest() throws CloneNotSupportedException, ClassCastException {
        SudokuField clone = (SudokuField) sudokuField2.clone();
        assertEquals(clone, sudokuField2);

        sudokuField2.setFieldValue(2);
        assertNotEquals(clone, sudokuField2);

        try {
            SudokuBoard clone1 = (SudokuBoard) sudokuField.clone();
        } catch (Exception e) { System.out.print(e);}
    }

    @Test
    void compareToTest() throws NullPointerException, ClassCastException {
        assertEquals(0, sudokuField.compareTo(sudokuField3));
        assertEquals(-1, sudokuField.compareTo(sudokuField2));
        assertTrue(sudokuField2.compareTo(sudokuField) > 0);

        try {
            SudokuField fieldNull = null;
            sudokuField.compareTo(fieldNull);
        } catch (Exception e) { System.out.print(e);}
    }
}
