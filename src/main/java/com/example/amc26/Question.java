package com.example.amc26;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Question {
    private Map<String, List<qst>> qstMap;
    private int currentIndexTheme = 0;

    public record qst(int id, String theme, String statement, boolean isTrue, String fact) {}

    public static void main(String[] args) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<qst>>() {}.getType();

        try (InputStream is = Question.class.getResourceAsStream("/qst.json")) {
            if (is == null) {
                throw new IllegalStateException("trivia.json not found in src/main/resources");
            }
            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                List<qst> items = gson.fromJson(reader, listType);

                System.out.println("Loaded items: " + items.size());
                for (qst item : items) {
                    System.out.println("[" + item.theme() + " #" + item.id() + "] " + item.statement() + " -> " + item.isTrue());
                }

                Map<String, List<qst>> byTheme = items.stream()
                        .collect(Collectors.groupingBy(qst::theme));

                List<String> themes = List.copyOf(byTheme.keySet());

                if (!themes.isEmpty()) {
                    int randomIndex = new Random().nextInt(themes.size());
                    String chosenTheme = themes.get(randomIndex);
                    List<qst> questionsForTheme = byTheme.get(chosenTheme);

                    System.out.println(randomIndex + ": " + chosenTheme + " (" + questionsForTheme.size() + " questions)");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Question() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<qst>>() {}.getType();
        qstMap = new HashMap<>();

        try (InputStream is = Question.class.getResourceAsStream("/qst.json")) {
            if (is == null) {
                throw new IllegalStateException("trivia.json not found in src/main/resources");
            }
            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                List<qst> items = gson.fromJson(reader, listType);
                qstMap = items.stream().collect(Collectors.groupingBy(qst::theme));
            }
        } catch (Exception e) {
            System.out.println("ERREUR : "+e.getMessage());
        }
    }

    public String getRandomTheme() {
        List<String> themes = List.copyOf(qstMap.keySet());

        if (!themes.isEmpty()) {
            currentIndexTheme = new Random().nextInt(themes.size());
            String chosenTheme = themes.get(currentIndexTheme);
            List<qst> questionsForTheme = qstMap.get(currentIndexTheme);

            return chosenTheme;
        } else {
            return "No theme for now";
        }
    }
}
