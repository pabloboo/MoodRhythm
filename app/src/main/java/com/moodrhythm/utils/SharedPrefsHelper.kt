package com.moodrhythm.utils

interface SharedPrefsHelper {
    fun getCurrentDayEmotionIdKey(): String
    fun getSharedPreferencesValueInt(key: String): Int
    fun setSharedPreferencesValueInt(key: String, value: Int)
    fun getSharedPreferencesValueString(key: String): String
    fun setSharedPreferencesValueString(key: String, value: String)
    fun getSharedPreferencesLocale(key: String): String
}