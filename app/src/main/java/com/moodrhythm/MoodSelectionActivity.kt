package com.moodrhythm

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.moodrhythm.model.Emotion
import com.moodrhythm.model.emotions
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar
import com.moodrhythm.utils.MockSharedPreferences
import com.moodrhythm.utils.SharedPrefsHelper
import com.moodrhythm.utils.SharedPrefsHelperImpl
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class MoodSelectionActivity : ComponentActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MoodRhythm)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                MainScreen(sharedPreferencesHelper = sharedPreferencesHelper)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MoodSelectionViewModel = MoodSelectionViewModel(), sharedPreferencesHelper: SharedPrefsHelper) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("d MMMM")
    val formattedDate = today.format(formatter)

    val context = LocalContext.current
    val selectedEmotion by viewModel.selectedEmotion.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .background(selectedEmotion.color)
                .verticalScroll(rememberScrollState())
        ) {
            CustomAppBar(
                contentColor = selectedEmotion.textColor
            )
            Text(
                text = LocalContext.current.getString(R.string.whats_your_mood_like_today).uppercase(),
                fontSize = 30.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = selectedEmotion.textColor,
                modifier = Modifier
            )
            Text(
                text = formattedDate,
                fontSize = 20.sp,
                color = selectedEmotion.textColor,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.size(16.dp))

            val halfSize = emotions.size / 2
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
            ) {
                emotions.take(halfSize).forEach { emotion ->
                    EmotionItem(emotion, selectedEmotion == emotion) {
                        viewModel.selectEmotion(emotion)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
            ) {
                emotions.drop(halfSize).forEach { emotion ->
                    EmotionItem(emotion, selectedEmotion == emotion) {
                        viewModel.selectEmotion(emotion)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }

            Image(
                painter = painterResource(selectedEmotion.imageId),
                contentDescription = "Selected emotion icon",
                modifier = Modifier.size(200.dp)
            )

            Button(
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                onClick = {
                    sharedPreferencesHelper.setSharedPreferencesValueInt(sharedPreferencesHelper.getCurrentDayEmotionIdKey(), selectedEmotion.id)
                    val intent = Intent(context, ResultsActivity::class.java)
                    context.startActivity(intent)
                }) {
                Text(
                    text = LocalContext.current.getString(R.string.continueStr),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
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
    val mockSharedPreferences = MockSharedPreferences()
    val sharedPreferencesHelper = SharedPrefsHelperImpl(mockSharedPreferences)
    MoodRhythmTheme {
        MainScreen(sharedPreferencesHelper = sharedPreferencesHelper)
    }
}