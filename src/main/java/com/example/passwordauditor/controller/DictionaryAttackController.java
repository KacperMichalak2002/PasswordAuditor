package com.example.passwordauditor.controller;

import com.example.passwordauditor.service.DictionaryAttack;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class DictionaryAttackController {

    private final SceneController sceneController = new SceneController();
    @FXML
    private PasswordField targetPassword;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private TextArea logArea;
    @FXML
    private Button backButton;

    private DictionaryAttack dictionaryAttackTask;
    private Thread dictionaryAttackThread;

    @FXML
    public void initialize(){
        stopButton.setDisable(true);
    }
    @FXML
    private void handleStart(){

        String password = targetPassword.getText();

        if(password == null || password.isEmpty()){
            return;
        }

        logArea.clear();

        dictionaryAttackTask = new DictionaryAttack(password);

        dictionaryAttackTask.messageProperty().addListener((observable, oldMsg, newMsg) ->{
            Platform.runLater(() -> {
                logArea.appendText(newMsg + "\n");
                logArea.setScrollTop(Double.MAX_VALUE);
            });
        });

        dictionaryAttackTask.setOnSucceeded(e ->{
            String result = dictionaryAttackTask.getValue();
            showAlert("Success", result);
            resetButtons();
        });

        dictionaryAttackTask.setOnCancelled(e ->{
            logArea.appendText("Test canceled by user \n");
            resetButtons();
        });

        dictionaryAttackTask.setOnFailed(e ->{
            showAlert("Error","Attack failed: " + dictionaryAttackTask.getException().getMessage());
            resetButtons();
        });

        dictionaryAttackThread = new Thread(dictionaryAttackTask);
        dictionaryAttackThread.setDaemon(true);
        dictionaryAttackThread.start();

    }

    @FXML
    private void handleStop(){
        if(dictionaryAttackTask != null){
            dictionaryAttackTask.cancel();
        }
    }


    private void showAlert(String title, String information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(information);
        alert.showAndWait();
    }

    private void resetButtons() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        backButton.setDisable(false);
        targetPassword.setDisable(false);
    }

    public void setTargetPassword(String password){
        targetPassword.setText(password);
    }

    @FXML
    public void handleBackButton(ActionEvent event){
        try{
            sceneController.switchToMainView(event);
        }catch (IOException e){
            System.err.println("Error changing scene to main view from brute force " + e.getMessage());
            e.printStackTrace();
        }

    }
}
