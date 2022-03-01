package pl.first.firstjava;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DifficultyLevel {

    public static final int BASIC_LEVEL = 5;

    public enum DifficultyLevelEnum {
        Easy,
        Medium,
        Hard,
        FromFile
    }


    private final Random rand = new Random();
    private final Set<FieldCoordinates> randomPositions = new HashSet<>();

    private void fillRandomPositionsList(int capacity) {
        for (int i = 0; i < capacity; i++) {
            boolean isAdded = false;
            while (!isAdded) {
                int x = rand.nextInt(9);
                int y = rand.nextInt(9);
                isAdded = randomPositions.add(new FieldCoordinates(x, y));
            }
        }
    }

    private void prepareFileBoard(SudokuBoard board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getPosition(i, j) == 0) {
                    randomPositions.add(new FieldCoordinates(i, j));
                }
            }
        }
    }

    public void chooseLevel(SudokuBoard board, String level)
            throws EmptyBoardException, UnknownLevelException {
        DifficultyLevelEnum difficultyLevelEnum = DifficultyLevelEnum.valueOf(level);
        if (!board.checkBoard() && ChoiceController.getSudokuBoardFromFile() == null) {
            throw new EmptyBoardException("Board wasn't filled");
        }
        switch (difficultyLevelEnum) {
            case Easy -> fillRandomPositionsList(BASIC_LEVEL * 3);

            case Medium -> fillRandomPositionsList(BASIC_LEVEL * 4);

            case Hard -> fillRandomPositionsList(BASIC_LEVEL * 5);

            case FromFile -> prepareFileBoard(ChoiceController.getSudokuBoardFromFile());

            default -> {
                return;
            }
        }

        for (FieldCoordinates field: randomPositions) {
            board.setPosition(field.getX(), field.getY(), 0);
        }
    }
}