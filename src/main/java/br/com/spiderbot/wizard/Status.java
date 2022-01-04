package br.com.spiderbot.wizard;

/**
 * Estado de operação do Wizard.
 */
public enum Status {
    
    /**
     * Estado de espera por uma ação do usuário.
     */
    IDLE,

    /**
     * Navegando entre as páginas.
     */
    NAVIGATING,

    /**
     * Executando a operação principal.
     */
    EXECUTING,

    /**
     * Usuário solicitou o cancelamento.
     */
    CANCELED, 

    /**
     * Processo concluído.
     */
    FINISHED;
}
