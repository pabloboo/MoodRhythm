package com.moodrhythm.utils

import android.content.Context
import com.moodrhythm.model.Emotion
import com.moodrhythm.model.findEmotionById
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getCurrentYear(): Int {
    return LocalDate.now().year
}

fun getYearlyMoodData(context: Context): List<Pair<LocalDate, Emotion>> {
    val yearlyMoodData = ArrayList<Pair<LocalDate, Emotion>>()
    val currentYear = LocalDate.now().year
    val startDate = LocalDate.of(currentYear, 1, 1)
    val endDate = LocalDate.of(currentYear, 12, 31)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var date = startDate
    while (!date.isAfter(endDate)) {
        val key = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + date.format(formatter)
        val moodId = getSharedPreferencesValueInt(context, key)
        if (moodId != -1) {
            val mood = findEmotionById(moodId)
            yearlyMoodData.add(Pair(date, mood))
        }
        date = date.plusDays(1)
    }

    return yearlyMoodData
}