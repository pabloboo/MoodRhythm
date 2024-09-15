package com.moodrhythm

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodrhythm.model.findEmotionById
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.getCurrentDayEmotionIdKey
import com.moodrhythm.utils.getSharedPreferencesValueInt

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

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .background(emotion.color)
        ) {
            Text(
                text = LocalContext.current.getString(R.string.your_are_feeling) + " " +
                        LocalContext.current.getString(emotion.name) + " " +
                        LocalContext.current.getString(R.string.today),
                modifier = Modifier.padding(16.dp),
                color = emotion.textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = LocalContext.current.getString(R.string.check_your_mood_playlist),
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoodRhythmTheme {
        ResultsScreen()
    }
}