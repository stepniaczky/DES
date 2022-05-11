package des;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Controller {

    private String key;
    private String keyFromFile;

    private String decrypted;
    private String decryptedFromFile;

    private String encrypted;
    private String encryptedFromFile;

    private final PopoutWindow popoutWindow = new PopoutWindow();

    @FXML
    private RadioButton fileRadioButton;
    @FXML
    private RadioButton windowRadioButton;
    @FXML
    private TextField keyTextField = new TextField();
    @FXML
    private TextField encryptedTextField = new TextField();
    @FXML
    private TextField decryptedTextField = new TextField();


    @FXML
    public void initialize() {

    }

    public void generateKey() {
        key = keyTextField.getText();
        System.out.print(key);
    }

    public void loadKey() throws IOException {
        keyFromFile = loadFile("Wczytaj klucz z pliku:");
    }

    public void saveKey() {
        saveFile("Zapisz klucz do pliku:", key);
    }

    public void openExplicitFile() throws IOException {
        decryptedFromFile = loadFile("Otwórz plik zawierający tekst jawny:");
    }

    public void openEncryptedFile() throws IOException {
        encryptedFromFile = loadFile("Otwórz plik zawierający szyfrogram:");
    }

    public void saveExplicitFile() {
        saveFile("Zapisz plik zawierający tekst jawny:", decrypted);
    }

    public void saveEncryptedFile() {
        saveFile("Zapisz plik zawierający szyfrogram:", encrypted);
    }

    public void encrypt() {
        if (windowRadioButton.isSelected()) {
            decrypted = decryptedTextField.getText();
            System.out.println("to do zaszyfrowania z okna");
            System.out.print(decrypted);
        } else {
            System.out.println("to do zaszyfrowania z pliku");
        }
    }

    public void decrypt() {
        if (windowRadioButton.isSelected()) {
            encrypted = encryptedTextField.getText();
            System.out.println("to do odszyfrowania z okna");
            System.out.print(encrypted);
        } else {
            System.out.println("to do odszyfrowania z pliku");
        }
    }

    public void saveFile(String title, String obj) {
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
            DaoFactory dao = new DaoFactory();
            Dao<String> file;
            file = dao.getFileDao(fileToSave.getAbsolutePath());
            file.write(obj);
        }
    }

    public String loadFile(String title) throws IOException {
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
            DaoFactory dao = new DaoFactory();
            Dao<String> file;
            file = dao.getFileDao(fileToOpen.getAbsolutePath());
            return file.read();
        }

        return null;
    }

    public void showAuthors() {
        String pom = "Mikołaj Stępniak 236659"
                + System.lineSeparator()
                + "Mateusz Przybył 236630";
        popoutWindow.messageBox("Autorzy programu",
                pom, Alert.AlertType.INFORMATION);
    }

    public void onActionFileRadioButton() {
        windowRadioButton.setSelected(false);
    }

    public void onActionWindowRadioButton() {
        fileRadioButton.setSelected(false);
    }

}