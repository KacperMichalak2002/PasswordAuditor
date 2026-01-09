module com.example.passwordauditor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.passwordauditor to javafx.fxml;
    exports com.example.passwordauditor;
}