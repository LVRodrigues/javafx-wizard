package io.github.lvrodrigues.wizard.pages;

import io.github.lvrodrigues.wizard.Constants;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
* Página de relatório de processamento da atividade principal do Wizard.
*
* @author $AuthorName$
* @author $CommitterName$
* @branch $Branch$
*/
public class Processing extends AbstractPage {

    /**
     * Apresenta o nome configurado nos parâmetros.
     */
    @FXML
    private Label labelName;

    /**
     * Apresenta o contador configurado nos parâmetros.
     */
    @FXML
    private Label labelCounter;

    /**
     * Barra de progresso para demonstrar o exemplo de execução do Wizard.
     */
    @FXML
    private ProgressIndicator indicator;

    /**
     * Propriedade para expor o progresso do processo em segundo plano.
     */
    private DoubleProperty progress;

    @Override
    public String getTitle() {
        return "Executar";
    }

    /**
     * Inicializar as propriedades.
     */
    @FXML
    public void initialize() {
        progress = new SimpleDoubleProperty(0);
        indicator.progressProperty().bind(progress);
    }

    @Override
    public void onShow() {
        super.onShow();
        labelName.setText(datas().get(Constants.PARAM_NAME));
        labelCounter.setText(datas().get(Constants.PARAM_COUNTER));
    }

    /**
     * Atualiza o progresso do processo em segundo plano.
     *
     * @param value Valor percentual, na faixa de 0 até 1.
     */
    public void setProgress(double value) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException(String.format("Valor %f está fora da faixa válida (0 até 1).", value));
        }
        progress.set(value);
    }
}
