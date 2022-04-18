package des;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Controller {

    private static SudokuBoard sudokuBoardFromFile;
    private final PopoutWindow popoutWindow = new PopoutWindow();

    @FXML
    private RadioButton fileRadioButton;
    @FXML
    private RadioButton windowRadioButton;


    @FXML
    public void initialize() {

    }

    public static SudokuBoard getSudokuBoardFromFile() {
        return sudokuBoardFromFile;
    }

    public void generateKey () {

    }

    public void loadKey () {
        loadFile("Wczytaj klucz z pliku:");
    }

    public void saveKey () {
        saveFile("Zapisz klucz do pliku:");
    }
    public void openExplicitFile () {
        loadFile("Otwórz plik zawierający tekst jawny:");
    }

    public void openEncryptedFile () {
        loadFile("Otwórz plik zawierający szyfrogram:");
    }

    public void saveExplicitFile () {
        saveFile("Zapisz plik zawierający tekst jawny:");
    }

    public void saveEncryptedFile () {
        saveFile("Zapisz plik zawierający szyfrogram:");
    }

    public void encrypt () {
//        if (windowRadioButton.isSelected()) {
//
//        } else {
//
//        }
    }

    public void decrypt () {
//        if (windowRadioButton.isSelected()) {
//
//        } else {
//
//        }
    }

    public void saveFile (String title) {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        int userSelection = fileChooser.showSaveDialog(parentFrame);


        if (userSelection == JFileChooser.ERROR_OPTION) {
            popoutWindow.messageBox("message_title",
                    "message_error", Alert.AlertType.WARNING);
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            SudokuBoardDaoFactory dao = new SudokuBoardDaoFactory();
            Dao<SudokuBoard> file;
            file = dao.getFileDao(fileToSave.getAbsolutePath());
//                file.write(boardCopy);
        }
    }

    public void loadFile(String title) {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        int userSelection = fileChooser.showOpenDialog(parentFrame);

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

    public void onActionFileRadioButton () {
        windowRadioButton.setSelected(false);
    }       // mozna usunac radio buttony i po prostu automatycznie zawartosc plikow wstawiac
            // do input fieldow ewentualnie nadpisywac ponownym zapisem do pliku

    public void onActionWindowRadioButton () {
        fileRadioButton.setSelected(false);
    }

}