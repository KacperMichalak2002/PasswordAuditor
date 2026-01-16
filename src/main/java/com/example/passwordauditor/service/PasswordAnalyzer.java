package com.example.passwordauditor.service;

import com.example.passwordauditor.model.PasswordAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class PasswordAnalyzer {

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[ !@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    private Set<String> commonPasswords;


    public PasswordAnalyzer(){
        this.commonPasswords = new HashSet<String>();
        loadCommonPasswordsFromFile();

    }

    private void loadCommonPasswordsFromFile() {

        String path = "/com/example/passwordauditor/data/10k-most-common.txt";

        try(InputStream inputStream = getClass().getResourceAsStream(path)){
            if(inputStream == null){
                System.err.println("Couldn't find file " + path);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
                String line;
                while((line = reader.readLine()) != null){
                    commonPasswords.add(line);
                }
            }
        }catch (IOException e){
            System.err.println("Can't load common passwords file " + e.getMessage());
            e.printStackTrace();
        }



    }

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

        if(containsCommonPassword(password)){
            score = 0;
            listOfStrengths.clear();
            listOfIssues.clear();
            listOfIssues.add("Password contains common password");
        }

        score = Math.max(0, Math.min(100,score));

        return new PasswordAnalysis(score,password.length(),passwordEntropy,listOfStrengths,listOfIssues);
    }

    private boolean containsCommonPassword(String password) {
        String lowerCasePassword = password.toLowerCase();

        return commonPasswords.contains(lowerCasePassword);
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
