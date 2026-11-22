package com.example.amc26;

import java.util.HashMap;

public class GameController {
    public Question currentQuestion;

    public String getTheme() {
        return currentQuestion.getRandomTheme();
    }

    public GameController() {
        currentQuestion = new Question();
    }
}
