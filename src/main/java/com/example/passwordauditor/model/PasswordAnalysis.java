package com.example.passwordauditor.model;

import java.util.List;

public class PasswordAnalysis {
    private int score;
    private int length;
    private List<String> listOfStrengths;
    private List<String> listOfIssues;

    public PasswordAnalysis(int score, int length, List<String> listOfStrengths, List<String> listOfIssues){
        this.score = score;
        this.length = length;
        this.listOfStrengths = listOfStrengths;
        this.listOfIssues = listOfIssues;
    }

    public int getScore() {
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
