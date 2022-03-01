package pl.first.firstjava;

import javafx.scene.control.Alert;

public class PopoutWindow {

    public void messageBox(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
