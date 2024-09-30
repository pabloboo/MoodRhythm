package com.moodrhythm.stats

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.moodrhythm.R
import com.moodrhythm.model.Emotion
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar
import com.moodrhythm.utils.MockSharedPreferences
import com.moodrhythm.utils.SharedPrefsHelper
import com.moodrhythm.utils.SharedPrefsHelperImpl
import com.moodrhythm.utils.StatsFunctions
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Month
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MonthlyStatsActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                MonthlyStatsScreen(this, sharedPreferencesHelper)
            }
        }
    }
}

@Composable
fun MonthlyStatsScreen(activity: Activity, sharedPreferencesHelper: SharedPrefsHelper) {
    val statsFunctions = StatsFunctions(sharedPreferencesHelper)
    var month by remember { mutableStateOf(LocalDate.now().month) }

    Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { innerPadding ->
        Column (Modifier.padding(innerPadding)) {
            CustomAppBar(
                onBackClick = {
                    val intent = Intent(activity, StatsActivity::class.java)
                    activity.startActivity(intent)
                }
            )
            Row (modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
                IconButton(onClick = { month = statsFunctions.previousMonth(month) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left), contentDescription = "Left arrow icon", Modifier.size(50.dp))
                }
                Text(
                    text = statsFunctions.getMonthName(month),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp).weight(1f)
                )
                IconButton(onClick = { month = statsFunctions.nextMonth(month) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right), contentDescription = "Right arrow icon", Modifier.size(50.dp))
                }
            }
            MoodMonthlyVisualizer(
                context = LocalContext.current,
                sharedPreferencesHelper = sharedPreferencesHelper,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                month = month
            )
        }
    }
}

@Composable
fun MoodMonthlyVisualizer(context: Context, sharedPreferencesHelper: SharedPrefsHelper, modifier: Modifier = Modifier, month: Month) {
    val statsFunctions = StatsFunctions(sharedPreferencesHelper)
    var monthlyData by remember { mutableStateOf<List<Pair<LocalDate, Emotion>>>(emptyList()) }

    LaunchedEffect(month) { // Update the data when the month changes
        monthlyData = statsFunctions.getMonthlyMoodData(month)
    }
    val grid = Array(6) { Array(7) { 0 } } // Maximum 6 weeks in a month
    val daysOfWeek = listOf(
        getString(context, R.string.monday),
        getString(context, R.string.tuesday),
        getString(context, R.string.wednesday),
        getString(context, R.string.thursday),
        getString(context, R.string.friday),
        getString(context, R.string.saturday),
        getString(context, R.string.sunday)
    )

    monthlyData.forEach { (date, emotion) ->
        val dayOfWeek = date.dayOfWeek.value - 1
        val weekFields = WeekFields.of(Locale.getDefault())
        val week = date.get(weekFields.weekOfMonth()) - 1
        grid[week][dayOfWeek] = emotion.imageId
    }

    LazyColumn (modifier = modifier) {
        item {
            Row {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier
                            .width(48.dp)
                            .background(MaterialTheme.colorScheme.surface),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        items(6) { week ->
            LazyRow {
                items(7) { dayOfWeek ->
                    val image = grid[week][dayOfWeek]
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        if (image != 0) {
                            Image(painter = painterResource(image), contentDescription = "Emotion image")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyStatsPreview() {
    val mockSharedPreferences = MockSharedPreferences()
    val sharedPreferencesHelper = SharedPrefsHelperImpl(mockSharedPreferences)
    MoodRhythmTheme {
        MonthlyStatsScreen(MonthlyStatsActivity(), sharedPreferencesHelper)
    }
}