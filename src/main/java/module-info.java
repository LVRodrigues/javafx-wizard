module br.com.spiderbot.wizard {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    exports br.com.spiderbot.wizard;
    opens br.com.spiderbot.wizard to javafx.fxml;
}