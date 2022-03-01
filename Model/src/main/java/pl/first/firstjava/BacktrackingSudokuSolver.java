package pl.first.firstjava;

import java.io.Serializable;
import java.util.Random;

public class BacktrackingSudokuSolver implements SudokuSolver, Serializable {

  @Override
  public void solve(SudokuBoard board) {
      fillDiagonal(board);
      fillRemaining(0, board.getSrn(), board);
  }

    /*Zaczynamy od wypełnienia trzech kwadratów 3x3 na boardzie
    po przekątnej całej planszy*/

    void fillDiagonal(SudokuBoard board) {
        for (int i = 0; i < board.getNmbr(); i = i + board.getSrn()) {
            fillBox(i, i, board);
        }
    }


    // Metoda wykorzysta 3 inne metody, żeby sprawdzić wszystkie 3 warunki
    // Sudoku dla liczby którą program
    // bedzie chciał wstawić
    boolean checkIfSafe(int i, int j, int num, SudokuBoard board) {
        return unUsedInRow(i, num, board)
                && unUsedInCol(j, num, board)
                && unUsedInBox(i - i % board.getSrn(), j - j % board.getSrn(), num, board);
    }

    // Sprawdzanie wiersza
    boolean unUsedInRow(int i, int num, SudokuBoard board) {
        for (int j = 0; j < board.getNmbr(); j++) {
            if (board.getPosition(i, j) == num) {
                return false;
            }
        }
        return true;
    }

    // Sprawdzanie kolumny
    boolean unUsedInCol(int j, int num, SudokuBoard board) {
        for (int i = 0; i < board.getNmbr(); i++) {
            if (board.getPosition(i, j) == num) {
                return false;
            }
        }
        return true;
    }

    // Metoda sprawdza czy dana liczba została już użyta w boxie 3x3
    boolean unUsedInBox(int x, int y, int num, SudokuBoard board) {
        for (int i = 0; i < board.getSrn(); i++) {
            for (int j = 0; j < board.getSrn(); j++) {
                if (board.getPosition(x + i, y + j) == num) {
                    return false;
                }
            }
        }
        return true;
    }

    // Wypełnianie jednego kwadratu 3x3
    void fillBox(int x, int y, SudokuBoard board) {
        Random rand = new Random();
        int num;
        for (int i = 0; i < board.getSrn(); i++) {
            for (int j = 0; j < board.getSrn(); j++) {
                do {
                    num = rand.nextInt(9) + 1;
                } while (!unUsedInBox(x, y, num, board));
                board.setPosition(x + i, y + j, num);
            }
        }
    }


    //Po wypełnieniu kwadratów "po przekątnej" w losowy sposób
    // wypełniamy resztę planszy
    boolean fillRemaining(int i, int j, SudokuBoard board) {
        //Spisujemy warunki, żeby rekurencja działała poprawnie
        if (j >= board.getNmbr() & i < board.getNmbr() - 1) {
            i++;
            j = 0;
        }

        //warunek kończący,
        if (i >= board.getNmbr() && j >= board.getNmbr()) {
            return true;
        }

        if (i < board.getSrn()) {
            if (j < board.getSrn()) {
                j = board.getSrn();
            }
        } else if (i < board.getNmbr() - board.getSrn()) {
            if (j == (int)(i / board.getSrn()) * board.getSrn()) {
                j = j + board.getSrn();
            }
        } else {
            if (j == board.getNmbr() - board.getSrn()) {
                i = i + 1;
                j = 0;
                if (i >= board.getNmbr()) {
                    return true;
                }
            }
        }

        //Zaczynamy sprawdzać i wypisywać
        for (int num = 1; num <= board.getNmbr(); num++) {
            if (checkIfSafe(i, j, num, board)) {
                board.setPosition(i, j, num);
                if (fillRemaining(i, j + 1, board)) {
                    return true;
                }
                board.setPosition(i, j, 0);
            }
        }
        return false;
    }


}
