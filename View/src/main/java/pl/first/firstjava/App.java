package pl.first.firstjava;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FxmlStageSetup.buildStage(stage, "/fxml/Scene.fxml",
                "Base64");
    }

    public static void main(String[] args) {
        launch();
    }
}
