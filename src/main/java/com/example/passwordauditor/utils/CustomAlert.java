package com.example.passwordauditor.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class CustomAlert {


    public static void showAlert(String title, String information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(information);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                CustomAlert.class.getResource("/com/example/passwordauditor/css/styles.css").toExternalForm());
        dialogPane.getStylesheets().add("myDialog");

        alert.showAndWait();
    }

}
