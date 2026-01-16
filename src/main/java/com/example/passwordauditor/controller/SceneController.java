package com.example.passwordauditor.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        closeStage(stage);
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.setOnCloseRequest(e ->{
            Platform.exit();
            System.exit(0);
        });
    }

    public void switchToBruteForceView(ActionEvent event) throws IOException{
        switchScene(event,"/com/example/passwordauditor/view/BruteForceWindow.fxml");
    }
}
