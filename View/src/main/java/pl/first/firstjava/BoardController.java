package pl.first.firstjava;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardController {

    public Button buttonCheck;
    public GridPane sudokuBoardGrid;
    private final PopoutWindow popoutWindow = new PopoutWindow();
    private SudokuBoard board = new SudokuBoard(9);
    private SudokuBoard boardCopy = new SudokuBoard(9);
    private final DifficultyLevel difficultyLevel = new DifficultyLevel();
    private final ResourceBundle bundle = ResourceBundle.getBundle("Language");
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class.getName());

    @FXML
    public void initialize() throws EmptyBoardException {
        if (ChoiceController.getSudokuBoardFromFile() != null) {
            board = ChoiceController.getSudokuBoardFromFile();
        }
        boardCopy = (SudokuBoard) board.clone();
        difficultyLevel.chooseLevel(boardCopy, ChoiceController.getLevel());
        fillGrid();
    }

    private void fillGrid() {
        StringConverter converter = new IntegerStringConverter();
        for (int i = 0; i < boardCopy.getNmbr(); i++) {
            for (int j = 0; j < boardCopy.getNmbr(); j++) {
                TextField textField = new TextField();
                int finalI = i;
                int finalJ = j;
                textField.addEventHandler(KeyEvent.KEY_TYPED, event -> {
                    if (!event.getCharacter().matches("[1-9]")
                            || textField.getText().length() == 1) {
                        event.consume();
                    } else {
                        boardCopy.setPosition(finalI, finalJ,
                                Integer.parseInt(event.getCharacter()));
                    }
                });
                textField.setMinSize(50, 50);
                textField.setFont(Font.font(18));
                IntegerProperty intProp = new SimpleIntegerProperty(boardCopy.getPosition(i, j));
                textField.textProperty().bindBidirectional(intProp, converter);
                if (boardCopy.getPosition(i, j) != 0) {
                    textField.setDisable(true);
                } else {
                    textField.setText("");
                }
                sudokuBoardGrid.add(textField, i, j);
            }
        }
    }

    @FXML
    public void onActionButtonCheck() throws IOException {
        if (boardCopy.checkBoard()) {
            logger.info(bundle.getString("log_won"));
            popoutWindow.messageBox(bundle.getString("check_title"),
                    bundle.getString("check_win"), Alert.AlertType.INFORMATION);
            FxmlStageSetup.buildStage("/fxml/Choice.fxml", bundle);
        } else {
            logger.info(bundle.getString("log_lost"));
            popoutWindow.messageBox(bundle.getString("check_title"),
                    bundle.getString("check_lose"), Alert.AlertType.INFORMATION);
        }
    }

    public void onActionButtonSaveFile() throws SQLException {
            JFrame parentFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(bundle.getString("message_file_save_title"));

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.CANCEL_OPTION) {
                logger.warn(bundle.getString("log_canceled_saving"));
                return;
            }

            if (userSelection == JFileChooser.ERROR_OPTION) {
                logger.error(bundle.getString("log_error_saving"));
                popoutWindow.messageBox(bundle.getString("message_title"),
                        bundle.getString("message_error"), Alert.AlertType.WARNING);
            }

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
                Dao<SudokuBoard> file;
                file = dao.getFileDao(fileToSave.getAbsolutePath());
                file.write(boardCopy);
                logger.info(bundle.getString("log_saved_file"));
            }
        }

    public String jdbcSaveDialog() throws SQLException {
        JdbcSudokuBoardDao openDialog = new JdbcSudokuBoardDao("openDialog");
        String text = openDialog.showAll()
                + System.lineSeparator()
                + System.lineSeparator()
                + bundle.getString("save_to_jdbc");

        return String.valueOf(
                JOptionPane.showInputDialog(text, bundle.getString("save_type_in")));
    }

        public void onActionButtonSaveJdbc() {
        String fileName;
            try {
                fileName = jdbcSaveDialog();
                if (!Objects.equals(fileName, "null")) {
                    SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
                    Dao<SudokuBoard> file;
                    file = dao.getJdbcDao(fileName);
                    file.write(boardCopy);
                    logger.info(bundle.getString("log_saved_jdbc"));
                } else {
                    logger.warn(bundle.getString("log_canceled_saving_jdbc"));
                }
            } catch (Exception e) {
                logger.error(bundle.getString("log_error_saving_jdbc"));
            }
        }

        public void  onActionButtonBack() throws Exception {
            FxmlStageSetup.buildStage("/fxml/Choice.fxml", bundle);
            ChoiceController.setLevel(null);
        }


}

