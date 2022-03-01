package pl.first.firstjava;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SudokuRow extends SudokuFields implements Cloneable, Serializable {
    public SudokuRow(final List<SudokuField> fields) {
        super(fields);
    }

    @Override
    protected Object clone() {
        List<SudokuField> fieldsClone = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            fieldsClone.add(new SudokuField());
            fieldsClone.get(i).setFieldValue(getFields().get(i).getFieldValue());
        }
        return new SudokuRow(fieldsClone);
    }
}
