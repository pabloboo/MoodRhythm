package com.moodrhythm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodrhythm.ui.theme.MoodRhythmTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var formattedDate: String
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("d MMMM")
        formattedDate = today.format(formatter)
    } else {
        formattedDate = "Today"
    }

    val selectedEmotion = remember { mutableStateOf(emotions[0]) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .background(selectedEmotion.value.color)
        ) {
            Text(
                text = LocalContext.current.getString(R.string.whats_your_mood_like_today).uppercase(),
                fontSize = 30.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = selectedEmotion.value.textColor,
                modifier = Modifier.padding(innerPadding)
            )
            Text(
                text = formattedDate,
                fontSize = 20.sp,
                color = selectedEmotion.value.textColor,
                modifier = Modifier.padding(innerPadding)
            )

            Spacer(modifier = Modifier.size(16.dp))

            val halfSize = emotions.size / 2
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(innerPadding)
            ) {
                emotions.take(halfSize).forEach { emotion ->
                    EmotionItem(emotion, selectedEmotion.value == emotion) {
                        selectedEmotion.value = emotion
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(innerPadding)
            ) {
                emotions.drop(halfSize).forEach { emotion ->
                    EmotionItem(emotion, selectedEmotion.value == emotion) {
                        selectedEmotion.value = emotion
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }

            Image(
                painter = painterResource(selectedEmotion.value.imageId),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
fun EmotionItem(emotion: Emotion, isSelected: Boolean, onSelected: () -> Unit) {
    val context = LocalContext.current
    Surface(
        color = if (isSelected) emotion.color else MaterialTheme.colorScheme.surface,
        contentColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, colorResource(R.color.black)),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .defaultMinSize(80.dp, 0.dp)
            .clip(RoundedCornerShape(50))
            .clickable { onSelected() }
    ) {
        Text(
            text = context.getString(emotion.name),
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            color = if (isSelected) emotion.textColor else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MoodRhythmTheme {
        MainScreen()
    }
}