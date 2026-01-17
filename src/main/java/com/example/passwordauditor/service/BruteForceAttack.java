package com.example.passwordauditor.service;

import javafx.concurrent.Task;

import java.util.Arrays;

public class BruteForceAttack extends Task<String> {

    private final String targetPassword;
    private final int maxLength;
    private final String charset;
    private long attemptCounter = 0;
    private long startTime;

    public static final String CHARSET_NUMERIC = "0123456789";
    public static final String CHARSET_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String CHARSET_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CHARSET_SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    public static final String CHARSET_ALPHANUMERIC = CHARSET_LOWERCASE + CHARSET_UPPERCASE + CHARSET_NUMERIC;
    public static final String CHARSET_ALL = CHARSET_ALPHANUMERIC + CHARSET_SPECIAL;

    public BruteForceAttack(String targetPassword, int maxLength, String charset){
        this.targetPassword = targetPassword;
        this.maxLength = maxLength;
        this.charset = charset;
    }

    @Override
    protected String call() throws Exception {

        startTime = System.currentTimeMillis();
        updateMessage("Starting...");

        updateMessage("Testing passwords of length " + maxLength + "...");
        String result = bruteForceLength(maxLength);

        return result;
    }

    private String bruteForceLength(int length) {
        char[] currentPassword = new char[length];
        Arrays.fill(currentPassword,charset.charAt(0));

        while(true){
            if(isCancelled()){
                return null;
            }

            String attemptPassword = new String(currentPassword);
            attemptCounter++;
            if(attemptCounter % 1000 == 0){

                long timeElapsed = System.currentTimeMillis() - startTime;
                double timeElapsedSeconds = (double) timeElapsed /1000;

                updateMessage(String.format("Testing: %s Attempts %,d TimeElapsed: %.0f/s",attemptPassword,attemptCounter,timeElapsedSeconds));

            }

            if(attemptPassword.equals(targetPassword)){
                return attemptPassword;
            }

            if(!incrementPassword(currentPassword)){
                break;
            }

        }
        return null;
    }

    private boolean incrementPassword(char[] currentPassword) {
        for(int i = currentPassword.length - 1; i >= 0; i--){
            int currentIndex = charset.indexOf(currentPassword[i]);

            if(currentIndex < charset.length() - 1){
                currentPassword[i] = charset.charAt(currentIndex + 1);
                return true;
            }else {
                currentPassword[i] = charset.charAt(0);

                if(i==0){
                    return false;
                }
            }
        }
        return false;
    }

}
