package com.example.passwordauditor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class BruteForceController {

    @FXML
    private PasswordField targetPassword;

    public void setTargetPassword(String password){
        targetPassword.setText(password);
    }


}
