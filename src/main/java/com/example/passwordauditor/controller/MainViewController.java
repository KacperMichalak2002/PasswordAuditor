package com.example.passwordauditor.controller;

import com.example.passwordauditor.model.PasswordAnalysis;
import com.example.passwordauditor.service.PasswordAnalyzer;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.Console;


public class MainViewController {

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

    private PasswordAnalyzer passwordAnalyzer;

    @FXML
    public void initialize(){
        passwordAnalyzer = new PasswordAnalyzer();

        passwordInputListener();

        analysisHBox.setVisible(false);
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

        Label titleLabel = new Label("Password Analysis:");
        analysisVBox.getChildren().add(titleLabel);

        Label lengthLabel = new Label("Length: " + passwordAnalysis.getLength());
        analysisVBox.getChildren().add(lengthLabel);

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

    }







}
