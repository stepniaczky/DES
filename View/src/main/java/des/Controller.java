package des;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Controller {

    private String key;

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
    private TextArea keyTextArea;
    @FXML
    private TextArea encryptedTextArea;
    @FXML
    private TextArea decryptedTextArea;


    @FXML
    public void initialize() {
        // zainicjalizowanie wartosci klucza i ustawienie go w TextFieldzie
    }

    public void generateKey() {
        key = keyTextArea.getText();
        System.out.print(key);
        // NAPISANIE DO KLUCZA
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
            decrypted = decryptedTextArea.getText();
            System.out.println("to do zaszyfrowania z okna");
            System.out.print(decrypted);

            DES des = new DES(decrypted, key);
        } else {
            System.out.println("to do zaszyfrowania z pliku");

            DES des = new DES(decryptedFromFile, key);
        }
        encrypted = decrypted + " chuj";
        encryptedTextArea.setText(encrypted);
    }

    public void decrypt() {
        if (windowRadioButton.isSelected()) {
            encrypted = encryptedTextArea.getText();
            System.out.println("to do odszyfrowania z okna");
            System.out.print(encrypted);

            DES des = new DES(encrypted, key);
        } else {
            System.out.println("to do odszyfrowania z pliku");

            DES des = new DES(encryptedFromFile, key);
        }

        decrypted = encrypted + " chuj";
        decryptedTextArea.setText(decrypted);
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