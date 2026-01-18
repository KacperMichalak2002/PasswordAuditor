package com.example.passwordauditor.controller;

import com.example.passwordauditor.model.PasswordAnalysis;
import com.example.passwordauditor.service.HIBPChecker;
import com.example.passwordauditor.service.PasswordAnalyzer;
import com.example.passwordauditor.utils.CustomAlert;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;



public class MainViewController {

    private static final SceneController sceneController = new SceneController();
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordStrengthLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private HBox analysisHBox;
    @FXML
    private VBox analysisVBox;
    @FXML
    private Button bruteForceButton;
    @FXML
    private Button dictionaryButton;
    @FXML
    private Button checkButton;

    private PasswordAnalyzer passwordAnalyzer;
    private HIBPChecker hibpChecker;

    @FXML
    public void initialize(){
        passwordAnalyzer = new PasswordAnalyzer();
        hibpChecker = new HIBPChecker();

        passwordInputListener();

        analysisHBox.setVisible(false);
        bruteForceButton.setDisable(true);
        dictionaryButton.setDisable(true);
        checkButton.setDisable(true);
    }

    private void passwordInputListener(){
        passwordField.textProperty().addListener((observable, oldValue, newValue) ->{
            handlePasswordChange(newValue);
        });
    }

    private void handlePasswordChange(String password){
       if(password == null || password.isEmpty()){
           resetUI();
           return;
       }

        PasswordAnalysis passwordAnalysis = passwordAnalyzer.analyzePassword(password);

        updateProgressBar(passwordAnalysis);
        updateAnalysis(passwordAnalysis);

        analysisHBox.setVisible(true);
        bruteForceButton.setDisable(false);
        dictionaryButton.setDisable(false);
        checkButton.setDisable(false);
    }

    private void updateProgressBar(PasswordAnalysis passwordAnalysis){

        double score = passwordAnalysis.getScore();
        double scoreForProgressBar = score / 100;
        progressBar.setProgress(scoreForProgressBar);

        String newLabel;
        String color;
        String barColor;

        if(score < 30){
            newLabel = "Weak password " + score + "%";
            color = "#a12828";
            barColor = "#a12828";
        }else if (score < 60){
            newLabel = "Medium password " + score + "%";
            color = "#edd626";
            barColor = "#edd626";
        }else{
            newLabel = "Strong password " + score + "%";
            color = "#3fd121";
            barColor = "#3fd121";
        }

        passwordStrengthLabel.setText(newLabel);
        passwordStrengthLabel.setTextFill(Color.web(color));
        progressBar.setStyle("-fx-accent:" + barColor +";");

    }

    private void updateAnalysis(PasswordAnalysis passwordAnalysis){
        analysisVBox.getChildren().clear();

        for(String info : passwordAnalysis.getListOfStrengths()){
            Label infoLabel = new Label(info);
            infoLabel.getStyleClass().add("strengthsLabel");
            analysisVBox.getChildren().add(infoLabel);
        }

        for(String info : passwordAnalysis.getListOfIssues()){
            Label infoLabel = new Label(info);
            infoLabel.getStyleClass().add("issuesLabel");
            analysisVBox.getChildren().add(infoLabel);
        }
    }

    private void resetUI(){
        progressBar.setProgress(0);
        passwordStrengthLabel.setText("Weak password 0%");
        passwordStrengthLabel.setTextFill(Color.web("#ffffff"));

        analysisHBox.setVisible(false);
        bruteForceButton.setDisable(true);
        dictionaryButton.setDisable(true);
        checkButton.setDisable(true);
    }

    @FXML
    public void handleOpenNewWindowBruteForce(ActionEvent event){
        try{
            sceneController.switchToBruteForceView(event,passwordField.getText());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleOpenNewWindowDictionary(ActionEvent event){
        try{
            sceneController.switchToDictionaryAttackView(event, passwordField.getText());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCheckButtonAction(){
        hibpChecker.checkPasswordLeak(passwordField.getText()).thenAccept(leakCount ->{
            Platform.runLater(() ->{
                if(leakCount > 0){
                    CustomAlert.showAlert("Password Leaked",String.format("Password leaked %d times", leakCount));
                }else if(leakCount == 0){
                    CustomAlert.showAlert("Safe","Password wasn't found");
                }
            });

        });
    }







}
