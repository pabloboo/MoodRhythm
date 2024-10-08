package com.moodrhythm.utils

import com.moodrhythm.model.Emotion
import com.moodrhythm.model.findEmotionById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class StatsFunctions @Inject constructor(
    private val sharedPreferencesHelper: SharedPrefsHelper
) {

    fun getCurrentYear(): Int {
        return LocalDate.now().year
    }

    fun getMonthName(month: Month): String {
        val monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale(sharedPreferencesHelper.getSharedPreferencesLocale(SharedPrefsConstants.LANGUAGE)))
        return monthName[0].uppercase() + monthName.substring(1)
    }

    fun previousMonth(month: Month): Month {
        return month.minus(1)
    }

    fun nextMonth(month: Month): Month {
        return month.plus(1)
    }

    suspend fun getMonthlyMoodData(month: Month): List<Pair<LocalDate, Emotion>> = withContext(Dispatchers.IO) {
        val monthlyMoodData = ArrayList<Pair<LocalDate, Emotion>>()
        val currentYear = LocalDate.now().year
        val currentMonth = month.value
        val startDate = LocalDate.of(currentYear, currentMonth, 1)
        val endDate = LocalDate.of(currentYear, currentMonth, startDate.lengthOfMonth())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var date = startDate
        while (!date.isAfter(endDate)) {
            val key = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + date.format(formatter)
            val moodId = sharedPreferencesHelper.getSharedPreferencesValueInt(key)
            if (moodId != -1) {
                val mood = findEmotionById(moodId)
                monthlyMoodData.add(Pair(date, mood))
            }
            date = date.plusDays(1)
        }

        return@withContext monthlyMoodData
    }

    suspend fun getYearlyMoodData(): List<Pair<LocalDate, Emotion>> = withContext(Dispatchers.IO) {
        val yearlyMoodData = ArrayList<Pair<LocalDate, Emotion>>()
        val currentYear = LocalDate.now().year
        val startDate = LocalDate.of(currentYear, 1, 1)
        val endDate = LocalDate.of(currentYear, 12, 31)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var date = startDate
        while (!date.isAfter(endDate)) {
            val key = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + date.format(formatter)
            val moodId = sharedPreferencesHelper.getSharedPreferencesValueInt(key)
            if (moodId != -1) {
                val mood = findEmotionById(moodId)
                yearlyMoodData.add(Pair(date, mood))
            }
            date = date.plusDays(1)
        }

        return@withContext yearlyMoodData
    }

}