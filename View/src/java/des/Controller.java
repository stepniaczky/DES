package des;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.apache.commons.codec.DecoderException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Controller {

    private String key = "0E329232EA6D0D73";

    private String decrypted;
    private String encrypted;

    private final PopoutWindow popoutWindow = new PopoutWindow();

    @FXML
    private TextArea keyTextArea;
    @FXML
    private TextArea encryptedTextArea;
    @FXML
    private TextArea decryptedTextArea;


    @FXML
    public void initialize() {
        keyTextArea.setText(key);
    }

    public void generateKey() {
        key = keyTextArea.getText();
    }

    public void openExplicitFile() throws IOException {
        String s = loadFile("Otwórz plik zawierający tekst jawny:");
        if (!Objects.equals(s, null))
            decrypted = s;
        decryptedTextArea.setText(decrypted);
    }

    public void openEncryptedFile() throws IOException {
        String s = loadFile("Otwórz plik zawierający szyfrogram:");
        if (!Objects.equals(s, null))
            encrypted = s;
        encryptedTextArea.setText(encrypted);
    }

    public void openKey() throws IOException {
        String s = loadFile("Otwórz plik zawierający klucz:");
        if (!Objects.equals(s, null))
            key = s;
        keyTextArea.setText(key);
    }

    public void saveExplicitFile() {
        decrypted = decryptedTextArea.getText();
        saveFile("Zapisz plik zawierający tekst jawny:", decrypted);
    }

    public void saveEncryptedFile() {
        encrypted = encryptedTextArea.getText();
        saveFile("Zapisz plik zawierający szyfrogram:", encrypted);
    }

    public void saveKey() {
        key = keyTextArea.getText();
        saveFile("Zapisz plik zawierający klucz:", key);
    }

    public void encrypt() throws DecoderException {
        decrypted = decryptedTextArea.getText();
        DES des = new DES(decrypted, key, false);
        encrypted = des.encrypt();
        encryptedTextArea.setText(encrypted);
    }

    public void decrypt() throws DecoderException {
        encrypted = encryptedTextArea.getText();
        DES des = new DES(encrypted, key, true);
        decrypted = des.encrypt();
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

            String s = file.read();
            if (Objects.equals(s, null)) {
                popoutWindow.messageBox("Error",
                        "File does not exist!", Alert.AlertType.WARNING);
            }
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

}