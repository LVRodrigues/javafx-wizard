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
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
public class Wizard implements Runnable {

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
     * Processo em segundo plano para monitoramento das páginas e gerenciamento 
     * da navegação.
     */
    private Thread monitor;

    /**
     * Controle de execução do processo em segundo plano.
     */
    private boolean monitoring;

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
            page    = introduction.getController();
            // Se desejar que as configurações sejam persistentes, carrege-as aqui...
            page.onShow();
            // Apresentando a primeira página:
            updatePage();

            // Ligando propriedades:
            labelStatus.textProperty().bind(status.asString());

            // Inicializando o monitoramento em segundo plano.
            monitoring  = true;
            monitor     = new Thread(this);
            monitor.start();
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
        int index = pages.size();
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
            // Alterando o conteúdo de apresentação (página).
            content.getChildren().clear();
            FXMLLoader loader = pages.get(current.get());
            content.getChildren().add(loader.getRoot());
            // Alterando o título da página:
            title.setText(page.getTitle());
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
        final int navigate = current.get() - 1;
        navigateTo(event, navigate);
    }

    /**
     * Evento de navegação para a próxima página.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void nextAction(ActionEvent event) {
        final int navigate = current.get() + 1;
        navigateTo(event, navigate);
    }

    /**
     * Executa a navegação para uma nova página.
     * @param event Evento de origem da navegação.
     * @param navigate Índice da próxima página.
     */
    private void navigateTo(ActionEvent event, final int navigate) {
        Scene scene = content.getScene();
        Service<Boolean> service = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        // Se é permitido navegar...
                        if (page != null) {
                            // Salva as configurações
                            page.onHide();
                            // Avança para a próxima página:
                            current.set(navigate);
                            // Carrega o novo controlador:
                            page = pages.get(current.get()).getController();
                            // Carregar os parâmetros:
                            page.onShow();
                        }
                        return Boolean.TRUE;
                    }
                };
            }
        };

        // Altera o cursor do mouse ao iniciar o processo em segundo plano.
        service.setOnRunning(r -> {
            scene.setCursor(Cursor.WAIT);
            status.set(Status.NAVIGATING);
        });

        // Apresenta uma mensagem em caso de falhas.
        service.setOnFailed(f -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(((Button) event.getSource()).getText());
            alert.setContentText(service.getException().getLocalizedMessage());
            alert.showAndWait();
            service.cancel();
            scene.setCursor(Cursor.DEFAULT);
            status.set(Status.IDLE);
        });   

        // Restaura o estado de espera por uma ação do usuário.
        service.setOnSucceeded(s -> {
            updatePage();
            scene.setCursor(Cursor.DEFAULT);
            status.set(Status.IDLE);
        });

        // Inicia o processo em segundo plano para troca de página.
        service.start();
    }

    /**
     * Evento de execução do processo específico do Wizard.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void executeAction(ActionEvent event) {
        status.set(Status.EXECUTING);
    }

    /**
     * Cancela a execução de processos do Wizard.
     * <p>
     * Também finaliza o aplicativo.
     * @param event Informações da origem do evento.
     */
    @FXML
    public void cancelAction(ActionEvent event) {
        shutdown();
        Platform.exit();
    }

    /**
     * Processo paralelo, executado em segundo plano.
     * <p>
     * Controlará os estados dos botões de navegação e operação do aplicativo.
     */
    @Override
    public void run() {
        while (monitoring) {
            previous.setDisable(
                page == null
                ||
                current.get() == 0
                ||
                !page.canDoPrevious()
                ||
                !status.get().equals(Status.IDLE)
            );

            next.setDisable(
                page == null
                ||
                current.get() == pages.size() - 1
                ||
                !page.canDoNext()
                ||
                !status.get().equals(Status.IDLE)
            );

            execute.setDisable(
                page == null
                ||
                current.get() != pages.size() - 1
                ||
                !status.get().equals(Status.IDLE)
            );

            Thread.yield();
        }
    }

    /**
     * Método para ser executado no fechamento do aplicativo,
     * para encerrar o processo em segundo plano.
     */
    public void shutdown() {
        monitoring = false;
        try {
            monitor.join();
        } catch (InterruptedException e) {
            // Já está encerrando... não precisa tratar a exceção...
            e.printStackTrace();
        }
    }

    /**
     * Expõe o estado de operação do aplicativo para o controle
     * de fechamento.
     * @return Status
     */
    public Status getStatus() {
        return status.get();
    }
}
