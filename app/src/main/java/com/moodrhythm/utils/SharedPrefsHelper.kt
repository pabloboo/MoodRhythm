package com.moodrhythm.utils

import android.content.SharedPreferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private fun getCurrentDay(): String {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return format.format(LocalDate.now())
    }

    fun getCurrentDayEmotionIdKey(): String {
        return SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + getCurrentDay()
    }

    fun getSharedPreferencesValueInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun setSharedPreferencesValueInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getSharedPreferencesValueString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun setSharedPreferencesValueString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSharedPreferencesLocale(key: String): String {
        return sharedPreferences.getString(key, "en") ?: "en"
    }
}