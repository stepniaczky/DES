package pl.first.firstjava;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class SudokuBoard implements Serializable, Cloneable {
    private final List<List<SudokuField>> board;
    private final int nmbr; // Liczba kolumn i wierszy
    private final int srn; // pierwiastek kwadratowy z nmbr
    private final SudokuSolver solver1 = new BacktrackingSudokuSolver();

    SudokuBoard(int n) {
        this.nmbr = n;
        // Obliczamy pierwiastek z nmbr, który przyda nam się później w funkcjach
        // Standardowo będzie to 3
        Double srnD = Math.sqrt(n);
        srn = srnD.intValue();
        board = Arrays.asList(new List[nmbr]);
        for (int i = 0; i < nmbr; i++) {
            board.set(i, Arrays.asList(new SudokuField[nmbr]));
        }
        // Wypelniamy listę żeby nie było NullException
        for (int i = 0; i < nmbr; i++) {
            for (int j = 0; j < nmbr; j++) {
                this.board.get(i).set(j, new SudokuField());
            }
        }
        solveGame();
    }


    public void solveGame() {
        solver1.solve(this);
    }

    //    public void printSudoku() {
    //        for (int i = 0; i < nmbr; i++) {
    //            for (int j = 0; j < nmbr; j++) {
    //                System.out.print(this.board.get(i).get(j).getFieldValue() + " ");
    //            }
    //            System.out.println();
    //        }
    //    }

    public List<List<SudokuField>> getBoard() {
        return this.board;
    }

    public int getNmbr() {
        return this.nmbr;
    }

    public int getSrn() {
        return this.srn;
    }

    public int getPosition(int x, int y) {
        return board.get(x).get(y).getFieldValue();
    }

    public void setPosition(int x, int y, int value) {
        board.get(x).get(y).setFieldValue(value);
    }

    public boolean checkBoard() {
        for (int i = 0; i < nmbr; i++) {
            if (!getRow(i).verify()) {
                return false;
            }
            if (!getColumn(i).verify()) {
                return false;
            }
        }
        for (int i = 0; i < nmbr; i += 3) {
            for (int j = 0; j < nmbr; j += 3) {
                if (!getBox(i, j).verify()) {
                    return false;
                }
            }
        }
        return true;
    }

    public SudokuRow getRow(int y) {
        List<SudokuField> fields = Arrays.asList(new SudokuField[SudokuFields.SIZE]);
        for (int i = 0; i < nmbr; i++) {
            fields.set(i, board.get(y).get(i));
        }
        return new SudokuRow(fields);
    }

    public SudokuColumn getColumn(int x) {
        List<SudokuField> fields = Arrays.asList(new SudokuField[SudokuFields.SIZE]);
        for (int i = 0; i < nmbr; i++) {
            fields.set(i, board.get(i).get(x));
        }
        return new SudokuColumn(fields);
    }

    public final SudokuBox getBox(int x, int y) {
        List<SudokuField> fields = Arrays.asList(new SudokuField[SudokuFields.SIZE]);
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++, k++) {
                fields.set(k, board.get(y + i).get(x + j));
            }
        }
        return new SudokuBox(fields);
    }

    @Override
    public String toString() {
        List<String> result = new ArrayList<>();
        for (List<SudokuField> field : board) {
            for (SudokuField field1 : field) {
                result.add(String.valueOf(field1.getFieldValue()));
            }
        }
        return MoreObjects.toStringHelper(this)
                .add("board", result)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
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
            final SudokuBoard otherBoard = (SudokuBoard) obj;
            return Objects.equals(board, otherBoard);
        }
        return false;
    }

    @Override
    public Object clone() {
        SudokuBoard boardClone = new SudokuBoard(nmbr);
        for (int i = 0; i < nmbr; i++) {
            for (int j = 0; j < nmbr; j++) {
                boardClone.setPosition(i, j, getPosition(i, j));
            }
        }
        return boardClone;
    }
}
