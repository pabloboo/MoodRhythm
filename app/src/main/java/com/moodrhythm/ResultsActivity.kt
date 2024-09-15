package com.moodrhythm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodrhythm.model.findEmotionById
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.getCurrentDayEmotionIdKey
import com.moodrhythm.utils.getSharedPreferencesValueInt
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                ResultsScreen()
            }
        }
    }
}

@Composable
fun ResultsScreen() {
    val context = LocalContext.current
    val emotionId = getSharedPreferencesValueInt(context, getCurrentDayEmotionIdKey())
    val emotion = findEmotionById(emotionId)
    val moodHistory = getMoodHistory(context)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .background(emotion.color)
        ) {
            Text(
                text = context.getString(R.string.your_are_feeling) + " " +
                        context.getString(emotion.name) + " " +
                        context.getString(R.string.today),
                modifier = Modifier.padding(16.dp),
                color = emotion.textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = context.getString(R.string.check_your_mood_playlist),
                modifier = Modifier.padding(12.dp),
                color = emotion.textColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(id = R.drawable.spotify_logo),
                contentDescription = "Spotify playlist url",
                modifier = Modifier.size(200.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = android.net.Uri.parse(emotion.moodPlaylist)
                        context.startActivity(intent)
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Display mood history
            Text(
                text = context.getString(R.string.mood_history),
                fontWeight = FontWeight.Bold,
                color = emotion.textColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn {
                items(moodHistory) { entry ->
                    Text(
                        text = entry,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// Function to get the mood history of the last week
fun getMoodHistory(context: Context): List<String> {
    val moodHistory = ArrayList<String>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var date = LocalDate.now().minusDays(6)

    repeat(7) {
        val key = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + date.format(formatter)
        val moodId = getSharedPreferencesValueInt(context, key)
        if (moodId != -1) {
            val mood = findEmotionById(moodId)
            moodHistory.add("Date: ${date.format(formatter)}, Mood: ${context.getString(mood.name)}")
        }
        date = date.plusDays(1)
    }

    return moodHistory
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoodRhythmTheme {
        ResultsScreen()
    }
}