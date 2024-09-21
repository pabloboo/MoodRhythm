package com.moodrhythm.utils

import android.content.SharedPreferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsHelperImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SharedPrefsHelper {

    private fun getCurrentDay(): String {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return format.format(LocalDate.now())
    }

    override fun getCurrentDayEmotionIdKey(): String {
        return SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + getCurrentDay()
    }

    override fun getSharedPreferencesValueInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    override fun setSharedPreferencesValueInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun getSharedPreferencesValueString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    override fun setSharedPreferencesValueString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getSharedPreferencesLocale(key: String): String {
        return sharedPreferences.getString(key, "en") ?: "en"
    }
}