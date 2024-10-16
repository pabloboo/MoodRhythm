package com.moodrhythm.utils

class FakeSharedPrefsHelper : SharedPrefsHelper {
    private val preferences = mutableMapOf<String, Any>()
    override fun getCurrentDay(): String {
        return "2024-01-01"
    }

    override fun getCurrentDayEmotionIdKey(): String {
        return SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + "2024-01-01"
    }

    override fun getDayBefore(date: String): String {
        return "2023-12-31"
    }

    override fun getEmotionIdKey(date: String): String {
        return SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + "2023-12-31"
    }

    override fun getSharedPreferencesLocale(key: String): String {
        return preferences[key] as? String ?: "en"
    }

    override fun getSharedPreferencesValueInt(key: String): Int {
        return preferences[key] as? Int ?: -1
    }

    override fun setSharedPreferencesValueInt(key: String, value: Int) {
        preferences[key] = value
    }

    override fun getSharedPreferencesValueString(key: String): String {
        return preferences[key] as? String ?: ""
    }

    override fun setSharedPreferencesValueString(key: String, value: String) {
        preferences[key] = value
    }
}
