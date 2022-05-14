package des;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String filePath = "/fxml/Scene.fxml";
        String appTitle = "DES";
        Object scene = new FXMLLoader(App.class.getResource(filePath)).load();
        stage.setScene(new Scene((Parent) scene));
        stage.setTitle(appTitle);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
