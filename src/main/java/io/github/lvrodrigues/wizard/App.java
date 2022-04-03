package io.github.lvrodrigues.wizard;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
* Inicialização do aplicativo.
*
* @author $Author$
* @author $Committer$
* @branch $Branch$
*/
public class App extends Application {

    /**
     * Método de inicialização da camada Java FX.
     *
     * @param stage Container principal da camada Java FX.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Wizard.class.getResource("wizard.fxml"));
    
        stage.setOnHidden(h -> {
            Wizard wizard = loader.getController();
            wizard.shutdown();
        });

        stage.setOnCloseRequest(c -> {
            Wizard wizard = loader.getController();
            if (wizard.getStatus() == Status.EXECUTING || wizard.getStatus() == Status.NAVIGATING) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Fecar");
                alert.setHeaderText("Aplicativo processando informação!");
                alert.setContentText("Fechamento solicitado não pode ser executado.");
                alert.showAndWait();
                c.consume();
            }
        });

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Java FX Wizard Example");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Inicialização do aplicativo Java.
     *
     * @param args Lista de argumentos recebidos pela linha de comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
