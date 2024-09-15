package com.moodrhythm.utils

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getCurrentDay(): String {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return format.format(LocalDate.now())
}

fun getCurrentDayEmotionIdKey(): String {
    return SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + getCurrentDay()
}

fun getSharedPreferencesValueInt(activity: Context, key: String): Int {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(key, -1)
}

fun setSharedPreferencesValueInt(activity: Context, key: String, value: Int) {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt(key, value)
    editor.apply()
}

fun getSharedPreferencesValueString(activity: Context, key: String): String {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, "") ?: ""
}

fun setSharedPreferencesValueString(activity: Context, key: String, value: String) {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getSharedPreferencesLocale(activity: Context, key: String): String {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, "en") ?: "en"
}