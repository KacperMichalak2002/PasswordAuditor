module com.example.passwordauditor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires javafx.base;
    requires java.net.http;

    opens com.example.passwordauditor to javafx.fxml;
    opens com.example.passwordauditor.controller to javafx.fxml;
    exports com.example.passwordauditor;
}