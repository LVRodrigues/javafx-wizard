package io.github.lvrodrigues.wizard.pages;

import io.github.lvrodrigues.wizard.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

/**
* Coleta de parâmetros.
*
* @author $AuthorName$
* @author $CommitterName$
* @branch $Branch$
*/
public class Parameters extends AbstractPage {

    /**
     * Valor padrão para o parâmetro <b>name</b> quando este ainda não
     * estiver atribuído.
     */
    private static final String PARAM_NAME_DEFAULT = "";

    /**
     * Valor padrão para o parâmetro <b>counter</b> quando este ainda
     * não estiver atribuído.
     *
     * <p>Será também o valor mínimo para o componente.
     */
    private static final Integer PARAM_COUNTER_DEFAULT = Integer.valueOf("1000");

    /**
     * Parâmetro nome para ser utilizado pelas próximas páginas.
     */
    @FXML
    private TextField fieldName;

    /**
     * Parêmtro de contagem de execuções.
     */
    @FXML
    private Spinner<Integer> fieldCounter;

    @Override
    public String getTitle() {
        return "Parâmetros";
    }

    @Override
    public void onHide() {
        datas().put(Constants.PARAM_NAME, fieldName.getText());
        datas().put(Constants.PARAM_COUNTER, fieldCounter.getValue().toString());
        super.onHide();
    }

    @Override
    public void onShow() {
        super.onShow();
        fieldName.setText(datas().getOrDefault(Constants.PARAM_NAME, PARAM_NAME_DEFAULT));
        fieldCounter.getValueFactory().setValue(
            Integer.valueOf(datas().getOrDefault(
                Constants.PARAM_COUNTER, PARAM_COUNTER_DEFAULT.toString())));
    }

    @Override
    public boolean canDoNext() {
        return !fieldName.getText().isBlank();
    }

    /**
     * Execução automática na inicialização do componente.
     */
    @FXML
    public void initialize() {
        fieldCounter.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(
                PARAM_COUNTER_DEFAULT, Integer.MAX_VALUE));
    }
}
