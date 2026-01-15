package com.example.passwordauditor.service;

import com.example.passwordauditor.model.PasswordAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordAnalyzer {

    public PasswordAnalysis analyzePassword(String password){
        int score = 0;
        List<String> listOfStrengths = new ArrayList<>();
        List<String> listOfIssues = new ArrayList<>();

        if(password.length()<6){
            listOfIssues.add("Password is too short");
        }else if(password.length()>12){
            score += 25;
            listOfIssues.add("Password is long enough");
        }else {
            score += 15;
        }

        if(Pattern.compile("[A-Z]").matcher(password).find()){
            score += 20;
            listOfStrengths.add("Password contains uppercase");
        }else{
            listOfIssues.add("Lacks uppercase");
        }

        if (Pattern.compile("[a-z]").matcher(password).find()) {
            score += 20;
            listOfStrengths.add("Password contains lowercase");
        }else {
            listOfIssues.add("Lacks lowercase");
        }

        if(Pattern.compile("[0-9]").matcher(password).find()){
            score += 20;
            listOfStrengths.add("Password contains number");
        }else {
            listOfIssues.add("Lacks number");
        }

        if(Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find()){
            score += 15;
            listOfStrengths.add("Password contains special character");
        }else{
            listOfIssues.add("Lacks special character");
        }

        return new PasswordAnalysis(score,password.length(),listOfStrengths,listOfIssues);
    }

}
