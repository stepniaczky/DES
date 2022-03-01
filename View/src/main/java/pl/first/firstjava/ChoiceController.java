package pl.first.firstjava;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChoiceController {

    public Button buttonStartGame;
    public Button buttonConfirmLevel;
    private static SudokuBoard sudokuBoardFromFile;

    private final PopoutWindow popoutWindow = new PopoutWindow();
    private static String level;
    public ResourceBundle bundle = ResourceBundle.getBundle("Language");

    Locale englishLocale = new Locale("en", "EN");
    private final ResourceBundle eng = ResourceBundle.getBundle("Language", englishLocale);
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class.getName());

//    @FXML
//    private ComboBox comboBoxSystemDifficult;

    @FXML
    public void initialize() throws IOException {
//        comboBoxSystemDifficult.getItems().add(bundle.getString("level_easy"));
//        comboBoxSystemDifficult.getItems().add(bundle.getString("level_medium"));
//        comboBoxSystemDifficult.getItems().add(bundle.getString("level_hard"));
    }

    public static String getLevel() {
        return level;
    }

    public static void setLevel(String level) {
        ChoiceController.level = level;
    }

    public static SudokuBoard getSudokuBoardFromFile() {
        return sudokuBoardFromFile;
    }

    public String getProperLevelName(String value) {
        if (bundle.getString("level_easy").equals(value)) {
            return eng.getString("level_easy");
        }
        if (bundle.getString("level_medium").equals(value)) {
            return eng.getString("level_medium");
        }
        if (bundle.getString("level_hard").equals(value)) {
            return eng.getString("level_hard");
        }
        if (bundle.getString("level_from_file").equals(value)) {
            return eng.getString("level_from_file");
        }
        return null;
    }

    public void onActionButtonConfirmLevel() {
        try {
//            String pom = comboBoxSystemDifficult.getSelectionModel().getSelectedItem();
//            level = getProperLevelName(pom);
        } catch (NullPointerException e) {
            popoutWindow.messageBox(bundle.getString("message_title"),
                    bundle.getString("message_info"), Alert.AlertType.WARNING);
        }
    }

    public void onActionButtonStartGame() throws IOException {
        if (!(level == null)) {
            if (bundle.getString("level_from_file").equals(level)) {
                logger.info(bundle.getString("log_file_game"));
            } else {
                logger.info(bundle.getString("log_new_game"));
            }
            FxmlStageSetup.buildStage("/fxml/Board.fxml", bundle);
        } else {
            popoutWindow.messageBox(bundle.getString("message_title"),
                    bundle.getString("message_info"), Alert.AlertType.WARNING);
        }
    }

    public void onActionButtonLoadFile() throws IOException, SQLException {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(bundle.getString("message_file_open_title"));

        int userSelection = fileChooser.showOpenDialog(parentFrame);

        if (userSelection == JFileChooser.CANCEL_OPTION) {
            logger.warn(bundle.getString("log_canceled_opening"));
            return;
        }

        if (userSelection == JFileChooser.ERROR_OPTION) {
            logger.error(bundle.getString("log_error_opening"));
            popoutWindow.messageBox(bundle.getString("message_title"),
                    bundle.getString("message_file_alert"), Alert.AlertType.WARNING);
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
            Dao<SudokuBoard> file;
            file = dao.getFileDao(fileToOpen.getAbsolutePath());
            sudokuBoardFromFile = file.read();
            level = getProperLevelName(bundle.getString("level_from_file"));
            logger.info(bundle.getString("log_opened"));
        }
    }

    public String jdbcOpenDialog() throws SQLException {
        JdbcSudokuBoardDao openDialog = new JdbcSudokuBoardDao("openDialog");
        String text = openDialog.showAll()
                + System.lineSeparator()
                + System.lineSeparator()
                + bundle.getString("open_from_jdbc");

        return String.valueOf(
                JOptionPane.showInputDialog(text, bundle.getString("open_type_in")));
    }

    public void onActionButtonLoadJdbc() {
        String fileName;
        try {
            fileName = jdbcOpenDialog();
            if (!Objects.equals(fileName, "null")) {
                SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
                Dao<SudokuBoard> file;
                file = dao.getJdbcDao(fileName);
                sudokuBoardFromFile = file.read();
                if (sudokuBoardFromFile == null) {
                    return;
                }
                level = getProperLevelName(bundle.getString("level_from_file"));
                logger.info(bundle.getString("log_opened_jdbc"));
            } else {
                logger.warn(bundle.getString("log_canceled_opening_jdbc"));
            }
        } catch (Exception e) {
            logger.error(bundle.getString("log_error_opening_jdbc"));
        }
    }

    public void onActionButtonAuthors() {
        String pom = bundle.getString("author_1")
                + System.lineSeparator()
                + bundle.getString("author_2");
        popoutWindow.messageBox(bundle.getString("message_authors_title"),
                pom, Alert.AlertType.WARNING);
}

    public void onActionButtonLangPl() throws IOException {
        Locale.setDefault(new Locale("pl","PL"));
        FxmlStageSetup.buildStage("/fxml/Choice.fxml", bundle);
        logger.info(bundle.getString("log_lang_pl"));
    }

    public void onActionButtonLangEn() throws IOException {
        Locale.setDefault(new Locale("en","EN"));
        FxmlStageSetup.buildStage("/fxml/Choice.fxml", bundle);
        logger.info(bundle.getString("log_lang_en"));
    }


}