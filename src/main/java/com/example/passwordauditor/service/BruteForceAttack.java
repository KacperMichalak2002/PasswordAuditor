package com.example.passwordauditor.service;

import javafx.concurrent.Task;

public class BruteForceAttack extends Task<String> {

    private final String targetPassword;
    private long attemptCounter = 0;

    public BruteForceAttack(String targetPassword){
        this.targetPassword = targetPassword;
    }

    @Override
    protected String call() throws Exception {
        return "";
    }
}
