package pl.first.firstjava;

import org.junit.jupiter.api.Test;

public class DifficultyLevelTest {

    @Test
    public void unknownLevelTest() throws UnknownLevelException, EmptyBoardException {
        SudokuBoard board = new SudokuBoard(9);
        DifficultyLevel difficultyLevel = new DifficultyLevel();
        difficultyLevel.chooseLevel(board, "unknownLevel");

    }
}