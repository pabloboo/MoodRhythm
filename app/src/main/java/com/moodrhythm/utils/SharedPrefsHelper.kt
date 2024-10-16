package com.moodrhythm.utils

interface SharedPrefsHelper {
    fun getCurrentDay(): String
    fun getCurrentDayEmotionIdKey(): String
    fun getDayBefore(date: String): String
    fun getEmotionIdKey(date: String): String
    fun getSharedPreferencesValueInt(key: String): Int
    fun setSharedPreferencesValueInt(key: String, value: Int)
    fun getSharedPreferencesValueString(key: String): String
    fun setSharedPreferencesValueString(key: String, value: String)
    fun getSharedPreferencesLocale(key: String): String
}