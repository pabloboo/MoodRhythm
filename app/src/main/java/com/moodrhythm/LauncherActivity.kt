package com.moodrhythm

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.moodrhythm.utils.getCurrentDayEmotionIdKey
import com.moodrhythm.utils.getSharedPreferencesValueInt

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (getSharedPreferencesValueInt(this, getCurrentDayEmotionIdKey()) == -1) {
            val intent = Intent(this, MoodSelectionActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
    }
}