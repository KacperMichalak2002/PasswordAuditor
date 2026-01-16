package com.example.passwordauditor.service;

import javafx.concurrent.Task;

public class BruteForceAttack extends Task<String> {

    private final String targetPassword;
    // Zestaw znaków - dla testów tylko małe litery i cyfry (szybciej znajdzie)
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private long attemptCounter = 0;

    public BruteForceAttack(String targetPassword) {
        this.targetPassword = targetPassword;
    }

    @Override
    protected String call() throws Exception {
        long startTime = System.currentTimeMillis();

        // Szukamy haseł o długości od 1 do 8 znaków
        for (int length = 1; length <= 8; length++) {
            if (isCancelled()) break;

            updateMessage("Generowanie haseł o długości: " + length);

            // Start rekurencji
            String result = bruteForceRecursive("", length);

            if (result != null) {
                long endTime = System.currentTimeMillis();
                double seconds = (endTime - startTime) / 1000.0;
                return "SUKCES! Znaleziono hasło: " + result + "\nCzas: " + seconds + "s\nLiczba prób: " + attemptCounter;
            }
        }

        return "Nie znaleziono hasła (limit długości lub przerwano).";
    }

    // Metoda rekurencyjna
    private String bruteForceRecursive(String currentGuess, int length) {
        if (isCancelled()) return null;

        if (length == 0) {
            attemptCounter++;

            // Aktualizacja UI co 10,000 prób (kluczowe dla wydajności!)
            if (attemptCounter % 10000 == 0) {
                updateMessage("Sprawdzam: " + currentGuess + " (Prób: " + attemptCounter + ")");
            }

            if (currentGuess.equals(targetPassword)) {
                return currentGuess; // ZNALEZIONO
            }
            return null;
        }

        for (int i = 0; i < CHARACTERS.length(); i++) {
            String nextGuess = currentGuess + CHARACTERS.charAt(i);
            String result = bruteForceRecursive(nextGuess, length - 1);
            if (result != null) return result;
            if (isCancelled()) return null;
        }

        return null;
    }
}