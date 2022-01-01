package br.com.spiderbot.wizard;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Wizard.class.getResource("wizard.fxml"));
    
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Java FX Wizard Example");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}