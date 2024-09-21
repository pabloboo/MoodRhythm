package com.moodrhythm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.text.intl.Locale
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.SharedPrefsHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locale = Locale.current.language
        sharedPreferencesHelper.setSharedPreferencesValueString(SharedPrefsConstants.LANGUAGE, locale)

        if (sharedPreferencesHelper.getSharedPreferencesValueInt(sharedPreferencesHelper.getCurrentDayEmotionIdKey()) == -1) {
            val intent = Intent(this, MoodSelectionActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
    }
}