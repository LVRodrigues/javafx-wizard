package br.com.spiderbot.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.spiderbot.wizard.pages.Introduction;
import br.com.spiderbot.wizard.pages.Page;
import br.com.spiderbot.wizard.pages.Parameters;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
* Classe de Gerenciamento do Wizard.
* <p>
* Controla a navegação entre as páginas do aplicativo.
*
* @author <a href="mailto:lvrodrigues@spiderbot.com.br">Luciano Vieira Rodrigues</a>
* @since 31/12/2021
* @author $$Author$$
* @version $$Revision$$ - $$Date$$
*/
public class Wizard {

    /**
     * Porcentagem de visibilidade do indicador da página corrente (100%).
     */
    private static final double INDICATOR_CURRENT = 1;

    /**
     * Porcentagem de visibilidade do indicador das outras páginas (20%).
     */
    private static final double INDICATOR_OTHERS = 0.2;
 
    /**
     * Botão para executar a navegação para página anterior.
     */
    @FXML
    private Button previous;

    /**
     * Botão para executar a navegação para a próxima página.
     */
    @FXML
    private Button next;

    /**
     * Quando na última página, permite executar uma ação específica do Wizard.
     */
    @FXML
    private Button execute;

    /**
     * Cancela a operação do Wizard.
     */
    @FXML
    private Button cancel;

    /**
     * Painel de informações sobre a página de navegação.
     */
    @FXML
    private FlowPane indicators;

    /**
     * Informa o estado de operação da página corrente.
     */
    @FXML
    private Label labelStatus;

    /**
     * Título da página corrente.
     */
    @FXML
    private Label title;

    /**
     * Painel para apresentação de conteúdo de cada página do Wizard.
     */
    @FXML
    private StackPane content;

    /**
     * Lista de páginas para gerenciar.
     */
    private List<FXMLLoader> pages;

    /**
     * Índice da página corrente.
     */
    private IntegerProperty current = new SimpleIntegerProperty(0);

    /**
     * Controlador da página corrente.
     */
    private Page page;

    /**
     * Estado de operação do Wizard.
     */
    private ObjectProperty<Status> status = new SimpleObjectProperty<Status>(Status.IDLE);

    /**
     * Inicialização da camada Java FX.
     * <p>
     * Utilize este método para carregar a lista de páginas do Wizard.
     */
    @FXML
    public void initialize() {
        pages = new ArrayList<>();

        try {
            FXMLLoader introduction = new FXMLLoader();
            introduction.setLocation(Introduction.class.getResource("introduction.fxml"));
            introduction.load();
            addPage(introduction);

            FXMLLoader parameters = new FXMLLoader();
            parameters.setLocation(Parameters.class.getResource("parameters.fxml"));
            parameters.load();
            addPage(parameters);

            // Duplicando páginas apenas para gerar volume:
            addPage(introduction);
            addPage(introduction);
            addPage(introduction);

            // Selecionando a página corrente:
            current.setValue(1);
            page    = introduction.getController();
            updatePage();

            // Inicializando o processo em segundo plano para monitoramento de navegação:
            labelStatus.textProperty().bind(status.asString());
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar as páginas do aplicativo.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
            System.exit(2);
        }
    }

    /**
     * Coloca a página de navegação na fila de apresentação.
     * <p>
     * Para cada pagína, é criado também um ícone de representação para
     * indicar o ponto de navegação do aplicativo.
     * 
     * @param page Página do Wizard.
     */
    private void addPage(FXMLLoader page) {
        int index = current.get();
        // Armazena a página para exibição.
        pages.add(index, page);
        // Adiciona um indicador de página sempre como primeiro componente.
        Circle circle = new Circle(5);
        circle.setFill(Color.BLACK);
        circle.opacityProperty().bind(
            new When(current.isEqualTo(index))
                .then(INDICATOR_CURRENT)
                .otherwise(INDICATOR_OTHERS));
        indicators.getChildren().add(index, circle);
        // Preparando-se para a próxima página:
        current.set(++index);
    }

    /**
     * Apresenta a página selecionada.
     */
    private void updatePage() {
        Scene scene = content.getScene();
        if (scene != null) {
            scene.setCursor(Cursor.WAIT);
        }
        try {
            content.getChildren().clear();
            FXMLLoader page = pages.get(current.get());
            content.getChildren().add(page.getRoot());
        } finally {
            if (scene != null) {
                scene.setCursor(Cursor.DEFAULT);
            }
        }
    }

    /**
     * Evento de navegação para a página anterior.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void previousAction(ActionEvent event) {

    }

    /**
     * Evento de navegação para a próxima página.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void nextAction(ActionEvent event) {

    }

    /**
     * Evento de execução do processo específico do Wizard.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void executeAction(ActionEvent event) {

    }

    /**
     * Cancela a execução de processos do Wizard.
     * <p>
     * Também finaliza o aplicativo.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void cancelAction(ActionEvent event) {
        Platform.exit();
    }
}
