package com.example.passwordauditor.controller;

import com.example.passwordauditor.service.BruteForceAttack;
import com.example.passwordauditor.utils.CustomAlert;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BruteForceController {

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

    private BruteForceAttack bruteForceTask;
    private Thread bruteForceThread;

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

        int maxLength = password.length();
        String charset = BruteForceAttack.CHARSET_ALL;

        logArea.clear();

        bruteForceTask = new BruteForceAttack(password, maxLength, charset);

        bruteForceTask.messageProperty().addListener((observable, oldValue, newValue) ->{
            Platform.runLater(() ->{
                logArea.appendText(newValue + "\n");
                logArea.setScrollTop(Double.MAX_VALUE);
            });
        });

        bruteForceTask.setOnSucceeded(e ->{
            String result = bruteForceTask.getValue();

            if(result != null){
                CustomAlert.showAlert("Success","Password found: " + result);
            }else {
                CustomAlert.showAlert("Result","Password not found");
            }

            resetButtons();
        });

        bruteForceTask.setOnFailed(e ->{
            CustomAlert.showAlert("Error", "Attack failed: " + bruteForceTask.getException().getMessage());
            resetButtons();
        });

        bruteForceTask.setOnCancelled(e->{
            logArea.appendText("Test cancelled by user \n");
            resetButtons();
        });

        bruteForceThread = new Thread(bruteForceTask);
        bruteForceThread.setDaemon(true);
        bruteForceThread.start();

        startButton.setDisable(true);
        stopButton.setDisable(false);
        backButton.setDisable(true);
        targetPassword.setDisable(true);

    }

    @FXML
    private void handleStop(){
        if(bruteForceTask != null){
            bruteForceTask.cancel();
            logArea.appendText("Stopping test...\n");
        }
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
