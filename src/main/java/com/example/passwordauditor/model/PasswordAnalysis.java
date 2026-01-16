package com.example.passwordauditor.model;

import java.util.List;

public class PasswordAnalysis {
    private double score;
    private int length;
    private List<String> listOfStrengths;
    private List<String> listOfIssues;
    private double passwordEntropy;

    public PasswordAnalysis(double score, int length,double passwordEntropy ,List<String> listOfStrengths, List<String> listOfIssues){
        this.score = score;
        this.length = length;
        this.passwordEntropy = passwordEntropy;
        this.listOfStrengths = listOfStrengths;
        this.listOfIssues = listOfIssues;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getPasswordEntropy() {
        return passwordEntropy;
    }

    public void setPasswordEntropy(double passwordEntropy) {
        this.passwordEntropy = passwordEntropy;
    }


    public List<String> getListOfStrengths() {
        return listOfStrengths;
    }

    public void setListOfStrengths(List<String> listOfStrengths) {
        this.listOfStrengths = listOfStrengths;
    }

    public List<String> getListOfIssues() {
        return listOfIssues;
    }

    public void setListOfIssues(List<String> listOfIssues) {
        this.listOfIssues = listOfIssues;
    }
}
