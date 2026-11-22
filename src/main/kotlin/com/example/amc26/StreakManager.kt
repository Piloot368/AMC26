package com.example.amc26

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StreakManager(private val filePath: Path) {

    data class StreakData(
        var streakCount: Int = 0,
        var bestStreak: Int = 0,
        var lastPlayedDate: String? = null
    )

    private var data: StreakData = loadData()

    private fun loadData(): StreakData {
        if (!Files.exists(filePath)) {
            return StreakData()
        }

        return try {
            Files.newBufferedReader(filePath, StandardCharsets.UTF_8).use { reader ->
                GSON.fromJson<StreakData>(reader, STREAK_DATA_TYPE) ?: StreakData()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            StreakData()
        }
    }

    fun saveData() {
        try {
            filePath.parent?.let { Files.createDirectories(it) }
            Files.newBufferedWriter(filePath, StandardCharsets.UTF_8).use { writer ->
                GSON.toJson(data, STREAK_DATA_TYPE, writer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getParsedLastPlayedDate(): LocalDate? {
        val rawDate = data.lastPlayedDate
        if (rawDate.isNullOrBlank() || rawDate.equals("null", ignoreCase = true)) {
            return null
        }
        return LocalDate.parse(rawDate)
    }

    fun checkStreakOnLaunch() {
        val lastDate = getParsedLastPlayedDate()
        val today = LocalDate.now()

        if (lastDate == null) {
            data.streakCount = 0
            return
        }

        if (today.isBefore(lastDate)) {
            return
        }

        val daysBetween = ChronoUnit.DAYS.between(lastDate, today)
        if (daysBetween > 1) {
            data.streakCount = 0
            saveData()
        }
    }

    fun recordAnsweredToday() {
        val lastDate = getParsedLastPlayedDate()
        val today = LocalDate.now()

        if (lastDate == null) {
            data.streakCount = 1
        } else {
            val daysBetween = ChronoUnit.DAYS.between(lastDate, today)
            if (daysBetween == 1L) {
                data.streakCount += 1
            } else if (daysBetween > 1L) {
                data.streakCount = 1
            }
        }

        data.lastPlayedDate = today.toString()

        if (data.streakCount > data.bestStreak) {
            data.bestStreak = data.streakCount
        }

        saveData()
    }

    val streakCount: Int
        get() = data.streakCount

    val bestStreak: Int
        get() = data.bestStreak

    val lastPlayedDate: String?
        get() = data.lastPlayedDate

    companion object {
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
        private val STREAK_DATA_TYPE: Type = object : TypeToken<StreakData>() {}.type
    }
}
