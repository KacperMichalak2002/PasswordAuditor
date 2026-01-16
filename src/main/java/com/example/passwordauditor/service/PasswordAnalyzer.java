package com.example.passwordauditor.service;

import com.example.passwordauditor.model.PasswordAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordAnalyzer {

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[ !@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    public PasswordAnalysis analyzePassword(String password){
        int score = 0;
        List<String> listOfStrengths = new ArrayList<>();
        List<String> listOfIssues = new ArrayList<>();

        double passwordEntropy = calculateEntropy(password);

        if(password.length()<6){
            listOfIssues.add("Password is too short (min. 6 chars)");
        }else if(password.length()>15){
            score += 25;
            listOfStrengths.add("Password is long enough");
        }else {
            score += 15;
            listOfStrengths.add("Password has good length");
        }

        if(UPPERCASE_PATTERN.matcher(password).find()){
            score += 20;
            listOfStrengths.add("Password contains uppercase");
        }else{
            listOfIssues.add("Lacks uppercase");
        }

        if (LOWERCASE_PATTERN.matcher(password).find()) {
            score += 20;
            listOfStrengths.add("Password contains lowercase");
        }else {
            listOfIssues.add("Lacks lowercase");
        }

        if(DIGIT_PATTERN.matcher(password).find()){
            score += 20;
            listOfStrengths.add("Password contains number");
        }else {
            listOfIssues.add("Lacks number");
        }

        if(SPECIAL_CHARS_PATTERN.matcher(password).find()){
            score += 15;
            listOfStrengths.add("Password contains special character");
        }else{
            listOfIssues.add("Lacks special character");
        }

        if(passwordEntropy >= 60){
            score += 20;
            listOfStrengths.add(String.format("Excellent entropy: %.0f bits", passwordEntropy));
        }else if(passwordEntropy >= 40){
            score += 10;
            listOfStrengths.add(String.format("Good entropy: %.0f bits", passwordEntropy));
        }else if(passwordEntropy >= 28){
            listOfIssues.add(String.format("Low entropy: %.0f bits", passwordEntropy));
        }else {
            score -= 20;
            listOfIssues.add(String.format("Very low entropy: %.0f bits", passwordEntropy));
        }


        score = Math.max(0, Math.min(100,score));

        return new PasswordAnalysis(score,password.length(),passwordEntropy,listOfStrengths,listOfIssues);
    }

    private double calculateEntropy(String password) {
        int sizeOfPassword = getPasswordSize(password);

        double entropy = password.length() * (Math.log(sizeOfPassword) / Math.log(2));

        return  entropy;
    }

    private int getPasswordSize(String password){

        int size = 0;

        if (LOWERCASE_PATTERN.matcher(password).find()){
            size += 26;
        }

        if(UPPERCASE_PATTERN.matcher(password).find()){
            size += 26;
        }

        if(DIGIT_PATTERN.matcher(password).find()){
            size += 10;
        }

        if(SPECIAL_CHARS_PATTERN.matcher(password).find()){
            size += 33;
        }

        return Math.max(1,size);

    }

}
