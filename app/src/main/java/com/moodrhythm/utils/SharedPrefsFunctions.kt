package com.moodrhythm.utils

import android.content.Context

fun getSharedPreferencesValueInt(activity: Context, key: String): Int {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(key, 0)
}

fun setSharedPreferencesValueInt(activity: Context, key: String, value: Int) {
    val sharedPreferences = activity.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt(key, value)
    editor.apply()
}