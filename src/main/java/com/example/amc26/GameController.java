package com.example.amc26;

public class GameController {
    public final int NB_QUESTIONS = 12;
    public Question currentQuestion;
    public Question.qst currentQst;
    public HelloApplication vue;

    public int currentIndexQuestion;
    public int nbCorrectAnswers = 0;

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

    public GameController(HelloApplication vue) {
        currentQuestion = new Question();
        currentIndexQuestion = 1;
        this.vue = vue;
    }

    public void nextQuestion() {
        currentQst = currentQuestion.getRandomQst();
        currentIndexQuestion++;
        if (currentIndexQuestion > NB_QUESTIONS) {
            vue.loadNewPlan(Plan.RESULTS);
        }
    }

    public void verifyAnswer(boolean buttonValue) {
        if (buttonValue == getTruthValue()) {
            nbCorrectAnswers++;
        }
    }
}
