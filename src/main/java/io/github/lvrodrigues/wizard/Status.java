package io.github.lvrodrigues.wizard;

/**
 * Estado de operação do Wizard.
 *
 * @author $Author$
 * @author $Committer$
 * @branch $Branch$
 */
public enum Status {
    
    /**
     * Estado de espera por uma ação do usuário.
     */
    IDLE(""),

    /**
     * Navegando entre as páginas.
     */
    NAVIGATING("Navegando"),

    /**
     * Executando a operação principal.
     */
    EXECUTING("Executando"),

    /**
     * Usuário solicitou o cancelamento.
     */
    CANCELED("Cancelado"), 

    /**
     * Processo concluído.
     */
    FINISHED("Concluído");

    /**
     * Descrição para apresentar ao usuário.
     */
    private String description;

    /**
     * Construtor oculto.
     *
     * @param description Descrição do estado.
     */
    Status(String description) {
        this.description = description;
    }

    /**
     * Sobrecarga do método para apresentar a descrição do estado.
     */
    @Override
    public String toString() {
        return description;
    }
}
