package com.example.passwordauditor.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.CompletableFuture;

public class HIBPChecker {

    private static final String API_URL = "https://api.pwnedpasswords.com/range/";

    public CompletableFuture<Integer> checkPasswordLeak(String password){
        return CompletableFuture.supplyAsync(()->{
            try{
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte [] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
                String hash = bytesToHex(bytes).toUpperCase();

                String prefix = hash.substring(0,5);
                String suffix = hash.substring(5);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + prefix))
                        .header("User-Agent","PasswordAuditor")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if(response.statusCode() == 200){
                    return parseResponse(response.body(), suffix);
                }else{
                    System.err.println("API ERROR: " + response.statusCode());
                    return -1;
                }
            }catch (Exception e){
                e.printStackTrace();
                return -1;
            }

        });

    }

    private int parseResponse(String body, String targetSuffix){
        String[] lines = body.split("\n");

        for(String line : lines){
            String[] fields = line.split(":");

            if(fields.length == 2){
                String hashSuffix = fields[0].trim();
                int count = Integer.parseInt(fields[1].trim());

                if(hashSuffix.equals(targetSuffix)){
                    return count;
                }
            }
        }
        return 0;
    }

    private String bytesToHex(byte [] bytes){
        StringBuffer sb = new StringBuffer();
        for(byte b: bytes){
            sb.append(String.format("%02x",b));
        }
        return sb.toString();
    }
}
