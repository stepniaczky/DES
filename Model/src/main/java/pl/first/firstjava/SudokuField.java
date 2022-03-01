package pl.first.firstjava;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.Objects;



public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    private int value;

    public SudokuField() {

    }

    public SudokuField(int value) {
        this.value = value;
    }

    public int getFieldValue() {
        return this.value;
    }

    public void setFieldValue(int value) {
        if (value >= 0 && value <= 9) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
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
            final SudokuField otherField = (SudokuField) obj;
            return Objects.equals(getFieldValue(), otherField.getFieldValue());
        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(SudokuField obj) throws NullPointerException, ClassCastException {
        if (obj == null) {
            throw new NullPointerException("You cannot compareTo null object");
        }
        return Integer.compare(this.getFieldValue(), obj.getFieldValue());
    }
}
