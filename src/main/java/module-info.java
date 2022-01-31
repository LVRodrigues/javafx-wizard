/**
 * Configuração de dependências do aplicativo.
 * 
 * @author $Author$
 * @author $Committer$
 * $Branch$
 */
module br.com.spiderbot.wizard {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.controls;    
    requires de.jensd.fx.glyphs.fontawesome;
    opens br.com.spiderbot.wizard to javafx.fxml;
    opens br.com.spiderbot.wizard.pages to javafx.fxml;
    exports br.com.spiderbot.wizard;
}