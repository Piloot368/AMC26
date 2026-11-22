package com.example.amc26;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StreakManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type STREAK_DATA_TYPE = new TypeToken<StreakData>() {}.getType();

    private final Path filePath;
    private StreakData data;

    public static class StreakData {
        public int streakCount;
        public int bestStreak;
        public String lastPlayedDate;
    }

    public StreakManager(Path filePath) {
        this.filePath = filePath;
        this.data = loadData();
    }

    private StreakData loadData() {
        StreakData defaultData = new StreakData();
        defaultData.streakCount = 0;
        defaultData.bestStreak = 0;
        defaultData.lastPlayedDate = null;

        if (!Files.exists(filePath)) {
            return defaultData;
        }

        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            StreakData loaded = GSON.fromJson(reader, STREAK_DATA_TYPE);
            if (loaded == null) {
                return defaultData;
            }
            return loaded;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultData;
        }
    }

    public void saveData() {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                GSON.toJson(data, STREAK_DATA_TYPE, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LocalDate getParsedLastPlayedDate() {
        if (data.lastPlayedDate == null || data.lastPlayedDate.isBlank()) {
            return null;
        }
        if ("null".equalsIgnoreCase(data.lastPlayedDate)) {
            return null;
        }
        return LocalDate.parse(data.lastPlayedDate);
    }

    public void checkStreakOnLaunch() {
        LocalDate lastDate = getParsedLastPlayedDate();
        LocalDate today = LocalDate.now();

        if (lastDate == null) {
            data.streakCount = 0;
            return;
        }

        if (today.isBefore(lastDate)) {
            return;
        }

        long daysBetween = ChronoUnit.DAYS.between(lastDate, today);
        if (daysBetween > 1) {
            data.streakCount = 0;
            saveData();
        }
    }

    public void recordAnsweredToday() {
        LocalDate lastDate = getParsedLastPlayedDate();
        LocalDate today = LocalDate.now();

        if (lastDate == null) {
            data.streakCount = 1;
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastDate, today);
            if (daysBetween == 1) {
                data.streakCount = data.streakCount + 1;
            } else if (daysBetween > 1) {
                data.streakCount = 1;
            }
        }

        data.lastPlayedDate = today.toString();

        if (data.streakCount > data.bestStreak) {
            data.bestStreak = data.streakCount;
        }

        saveData();
    }

    public int getStreakCount() {
        return data.streakCount;
    }

    public int getBestStreak() {
        return data.bestStreak;
    }

    public String getLastPlayedDate() {
        return data.lastPlayedDate;
    }
}
