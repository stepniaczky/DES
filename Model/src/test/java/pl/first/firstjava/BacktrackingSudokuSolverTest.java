package pl.first.firstjava;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BacktrackingSudokuSolverTest {

    public BacktrackingSudokuSolverTest(){

    }

    @Test
    public void doesntRepeat(){
        SudokuBoard board1 = new SudokuBoard(9);
        SudokuBoard board2 = new SudokuBoard(9);
        assertNotNull(board1.getBoard());
        assertNotNull(board2.getBoard());
        assertFalse(board1.getBoard() == board2.getBoard());
    }

    @Test
    public void isValid(){
        SudokuBoard board = new SudokuBoard(9);
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        board.checkBoard();
//        for(int i = 0; i < board.getNmbr(); i++){
//            for(int j = 0; j < board.getNmbr(); j++){
//                assertFalse(solver.checkIfSafe(i, j, board.getPosition(i, j), board));
//            }
//        }
    }
}

// Dopisać testy do row column box
