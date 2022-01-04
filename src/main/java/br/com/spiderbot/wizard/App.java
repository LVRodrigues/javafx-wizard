package br.com.spiderbot.wizard;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
* Inicialização do aplicativo.
*
* @author <a href="mailto:lvrodrigues@spiderbot.com.br">Luciano Vieira Rodrigues</a>
* @since 31/12/2021
* @author $$Author$$
* @version $$Revision$$ - $$Date$$
*/
public class App extends Application {

    /**
     * Método de inicialização da camada Java FX.
     * @param Stage Container principal da camada Java FX.
     */
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

    /**
     * Inicialização do aplicativo Java.
     * @param args Lista de argumentos recebidos pela linha de comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}