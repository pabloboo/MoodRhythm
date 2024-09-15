package com.moodrhythm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.text.intl.Locale
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.getCurrentDayEmotionIdKey
import com.moodrhythm.utils.getSharedPreferencesValueInt
import com.moodrhythm.utils.setSharedPreferencesValueString

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val locale = Locale.current.language
        setSharedPreferencesValueString(this, SharedPrefsConstants.LANGUAGE, locale)

        if (getSharedPreferencesValueInt(this, getCurrentDayEmotionIdKey()) == -1) {
            val intent = Intent(this, MoodSelectionActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
    }
}