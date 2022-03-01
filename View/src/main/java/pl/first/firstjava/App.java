package pl.first.firstjava;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    @Override
    public void start(Stage stage) throws IOException {
        FxmlStageSetup.buildStage(stage, "/fxml/Choice.fxml",
                bundle.getString("game_title"), bundle);
    }

    public static void main(String[] args) {
        launch();
    }
}
