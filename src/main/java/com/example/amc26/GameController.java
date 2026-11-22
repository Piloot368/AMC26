package com.example.amc26;

public class GameController {
    public final int NB_QUESTIONS = 12;
    public Question currentQuestion;
    public Question.qst currentQst;

    public int currentIndexQuestion = 0;

    public String getTheme() {
        return currentQst.theme();
    }

    public String getStatement() {
        return currentQst.statement();
    }

    public boolean getTruthValue() {
        return currentQst.isTrue();
    }

    public String getTruth() {
        if (currentQst.isTrue()) {
            return "Vrai. ";
        } else {
            return "Faux. ";
        }
    }

    public String getFact() {
        return currentQst.fact();
    }

    public GameController() {
        currentQuestion = new Question();
    }

    public void nextQuestion() {
        currentQst = currentQuestion.getRandomQst();
    }
}
