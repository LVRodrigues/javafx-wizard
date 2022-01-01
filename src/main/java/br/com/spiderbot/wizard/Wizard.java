package br.com.spiderbot.wizard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

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
    private Label status;

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
}
