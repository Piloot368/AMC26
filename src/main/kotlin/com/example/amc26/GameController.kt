package com.example.amc26

class GameController(val vue: HelloApplication) {
    var currentQuestion: Question = Question()
    var currentQst: Question.Qst? = null
    var currentIndexQuestion: Int = 1
    var nbCorrectAnswers: Int = 0

    fun getTheme(): String = currentQst?.theme.orEmpty()

    fun getStatement(): String = currentQst?.statement.orEmpty()

    fun getTruthValue(): Boolean = currentQst?.isTrue ?: false

    fun getTruth(): String = if (getTruthValue()) "Vrai. " else "Faux. "

    fun getFact(): String = currentQst?.fact.orEmpty()

    fun nextQuestion() {
        currentQst = currentQuestion.getRandomQst()
        currentIndexQuestion++
        if (currentIndexQuestion > NB_QUESTIONS) {
            vue.loadNewPlan(Plan.RESULTS)
        }
    }

    fun verifyAnswer(buttonValue: Boolean) {
        if (buttonValue == getTruthValue()) {
            nbCorrectAnswers++
        }
    }

    companion object {
        const val NB_QUESTIONS: Int = 12
    }
}
