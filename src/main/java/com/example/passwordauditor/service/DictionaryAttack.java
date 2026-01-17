package com.example.passwordauditor.service;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DictionaryAttack extends Task<String> {

    private final String targetPassword;
    private long lineCounter = 0;

    private static final String [] FILES = {
            "10k-most-common.txt",
            "100k-most-used-passwords-NCSC.txt",
            "Polish-common-password-list.txt"
    };

    public DictionaryAttack(String targetPassword){
        this.targetPassword = targetPassword;
    }

    @Override
    protected String call() throws Exception {
        long startTime = System.currentTimeMillis();
        updateMessage("Start...");

        for(String fileName : FILES){
            String path = "/com/example/passwordauditor/data/" + fileName;

            try(InputStream inputStream = getClass().getResourceAsStream(path)){
                if(inputStream == null){
                    return "Error reading file";
                }

                try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
                    String line;

                    while((line = reader.readLine()) !=null){
                        if(isCancelled()){
                            return "Stopped by user";
                        }

                        lineCounter++;
                        String word = line.trim();

                        if(lineCounter % 500 == 0){
                            updateMessage("Testing word #" + lineCounter + ": " + word);
                        }

                        if(word.equals(targetPassword)){
                            long endTime = System.currentTimeMillis();
                            double time = (endTime - startTime) /  1000.0;

                            return String.format("Success, password was found\n Password: %s \n Line: %d \n Time %.4fs",word,lineCounter,time);
                        }
                    }
                }
            }catch (Exception e){
                return "Error reading file " + e.getMessage();
            }
        }

        return String.format("Password was not found \n Line read: %d",lineCounter);
    }
}
