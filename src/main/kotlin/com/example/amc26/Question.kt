package com.example.amc26

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import kotlin.random.Random

class Question {
    private var qstMap: Map<String, List<Qst>> = emptyMap()
    private var currentIndexTheme: Int = 0
    private var currentTheme: String? = null
    private var currentQst: Qst? = null

    data class Qst(
        val id: Int = 0,
        val theme: String = "",
        val statement: String = "",
        val isTrue: Boolean = false,
        val fact: String = ""
    )

    init {
        val gson = Gson()
        val listType = object : TypeToken<List<Qst>>() {}.type

        try {
            val inputStream = Question::class.java.getResourceAsStream("/qst.json")
                ?: throw IllegalStateException("trivia.json not found in src/main/resources")

            inputStream.use { stream ->
                InputStreamReader(stream, StandardCharsets.UTF_8).use { reader ->
                    val items: List<Qst> = gson.fromJson(reader, listType) ?: emptyList()
                    qstMap = items.groupBy { it.theme }
                }
            }
        } catch (e: Exception) {
            println("ERREUR : ${e.message}")
        }
    }

    fun getRandomQst(): Qst? {
        val themes = qstMap.keys.toList()
        if (themes.isEmpty()) return null

        currentIndexTheme = Random.nextInt(themes.size)
        val chosenTheme = themes[currentIndexTheme]
        currentTheme = chosenTheme

        val themeQuestions = qstMap[chosenTheme].orEmpty()
        if (themeQuestions.isEmpty()) return null

        val index = Random.nextInt(themeQuestions.size)
        currentQst = themeQuestions[index]
        return currentQst
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val gson = Gson()
            val listType = object : TypeToken<List<Qst>>() {}.type

            try {
                val inputStream = Question::class.java.getResourceAsStream("/qst.json")
                    ?: throw IllegalStateException("trivia.json not found in src/main/resources")

                inputStream.use { stream ->
                    InputStreamReader(stream, StandardCharsets.UTF_8).use { reader ->
                        val items: List<Qst> = gson.fromJson(reader, listType) ?: emptyList()

                        println("Loaded items: ${items.size}")
                        for (item in items) {
                            println("[${item.theme} #${item.id}] ${item.statement} -> ${item.isTrue}")
                        }

                        val byTheme = items.groupBy { it.theme }
                        val themes = byTheme.keys.toList()

                        if (themes.isNotEmpty()) {
                            val randomIndex = Random.nextInt(themes.size)
                            val chosenTheme = themes[randomIndex]
                            val questionsForTheme = byTheme[chosenTheme].orEmpty()

                            println("$randomIndex: $chosenTheme (${questionsForTheme.size} questions)")
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}
