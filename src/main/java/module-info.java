module br.com.spiderbot.wizard {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    opens br.com.spiderbot.wizard to javafx.fxml;
    opens br.com.spiderbot.wizard.pages to javafx.fxml;
    exports br.com.spiderbot.wizard;
}