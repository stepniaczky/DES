package pl.first.firstjava;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Controller {

    private static SudokuBoard sudokuBoardFromFile;
    private final PopoutWindow popoutWindow = new PopoutWindow();

    @FXML
    public void initialize() {

    }

    public static SudokuBoard getSudokuBoardFromFile() {
        return sudokuBoardFromFile;
    }

    public void generateKey () {

    }

    public void loadKey () {

    }

    public void saveKey () {

    }
    public void openExplicitFile () {

    }

    public void openEncryptedFile () {

    }

    public void saveExplicitFile () {

    }

    public void saveEncryptedFile () {

    }

    public void encrypt () {

    }

    public void decrypt () {

    }

    public void onActionButtonLoadFile() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("message_file_open_title");

        int userSelection = fileChooser.showOpenDialog(parentFrame);

        if (userSelection == JFileChooser.CANCEL_OPTION) {
            return;
        }

        if (userSelection == JFileChooser.ERROR_OPTION) {
            popoutWindow.messageBox("message_title",
                    "message_file_alert", Alert.AlertType.WARNING);
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
            Dao<SudokuBoard> file;
            file = dao.getFileDao(fileToOpen.getAbsolutePath());
            sudokuBoardFromFile = file.read();
        }
    }

    public void showAuthors() {
        String pom = "Mikołaj Stępniak 236659"
                + System.lineSeparator()
                + "Mateusz Przybył 236630";
        popoutWindow.messageBox("Autorzy programu",
                pom, Alert.AlertType.INFORMATION);
    }

}