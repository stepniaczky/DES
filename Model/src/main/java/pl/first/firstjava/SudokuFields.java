package pl.first.firstjava;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SudokuFields {
    protected static final int SIZE = 9;
    protected List<SudokuField> fields;

    public SudokuFields(List<SudokuField> fields) {
        if (fields.size() == SIZE) {
            this.fields = fields;
        }
    }

    public List<SudokuField> getFields() {
        return fields;
    }

    public void setFields(List<SudokuField> fields) {
        this.fields = fields;
    }

    public boolean verify() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = i + 1; j < SIZE; j++) {
                if (this.fields.get(i).getFieldValue() == this.fields.get(j).getFieldValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        List<String> result = new ArrayList<>();
        for (SudokuField field : fields) {
            result.add(String.valueOf(field.getFieldValue()));
        }
        return MoreObjects.toStringHelper(this)
                .add("fields", result)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass().equals(obj.getClass())) {
            final SudokuFields otherFields = (SudokuFields) obj;
            return Objects.equals(fields, otherFields);
        }
        return false;
    }
}
