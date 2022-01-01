package br.com.spiderbot.wizard.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

/**
* Coleta de parâmetros.
*
* @author <a href="mailto:lvrodrigues@spiderbot.com.br">Luciano Vieira Rodrigues</a>
* @since 01/01/2022
* @author $$Author$$
* @version $$Revision$$ - $$Date$$
*/
public class Parameters extends Page {

    /**
     * Parâmetro nome.
     */
    public static final String PARAM_NAME = "name";

    /**
     * Valor padrão para o parâmetro <b>name</b> quando este ainda não
     * estiver atribuído.
     */
    private static final String PARAM_NAME_DEFAULT = "";

    /**
     * Parâmetro contador.
     */
    public static final String PARAM_COUNTER = "counter";

    /**
     * Valor padrão para o parâmetro <b>counter</b> quando este ainda
     * não estiver atribuído.
     * <p>
     * Será também o valor mínimo para o componente.
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
    public void onShow() {
        fieldName.setText(datas().getOrDefault(PARAM_NAME, PARAM_NAME_DEFAULT));
        fieldCounter.getValueFactory().setValue(Integer.valueOf(datas().getOrDefault(PARAM_COUNTER, PARAM_COUNTER_DEFAULT.toString())));
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
        fieldCounter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(PARAM_COUNTER_DEFAULT, Integer.MAX_VALUE));
    }
}
