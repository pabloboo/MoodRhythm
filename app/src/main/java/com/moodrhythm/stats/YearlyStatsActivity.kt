package com.moodrhythm.stats

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodrhythm.R
import com.moodrhythm.model.Emotion
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar
import com.moodrhythm.utils.MockSharedPreferences
import com.moodrhythm.utils.PieChart
import com.moodrhythm.utils.SharedPreferencesHelper
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.StatsFunctions
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Month
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class YearlyStatsActivity : ComponentActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                YearlyStatsScreen(this, sharedPreferencesHelper)
            }
        }
    }
}

@Composable
fun YearlyStatsScreen(activity: Activity, sharedPreferencesHelper: SharedPreferencesHelper) {
    val context = LocalContext.current
    val statsFunctions = StatsFunctions(sharedPreferencesHelper)
    var moodData by remember { mutableStateOf<List<Pair<LocalDate, Emotion>>>(emptyList()) }
    LaunchedEffect(Unit) {
        moodData = statsFunctions.getYearlyMoodData()
    }
    var shownGraph by remember { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { innerPadding ->
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(innerPadding)) {
            CustomAppBar(
                onBackClick = {
                    val intent = Intent(activity, StatsActivity::class.java)
                    activity.startActivity(intent)
                }
            )
            Text(
                text = statsFunctions.getCurrentYear().toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Row (modifier = Modifier.align(Alignment.CenterHorizontally)) { // buttons for switching between charts
                if (shownGraph == 0) {
                    Button(onClick = { shownGraph = 0 }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)) {
                        Text(text = context.getString(R.string.pie_chart))
                    }
                } else {
                    Button(onClick = { shownGraph = 0 }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface)) {
                        Text(text = context.getString(R.string.pie_chart))
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (shownGraph == 1) {
                    Button(onClick = { shownGraph = 1 }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)) {
                        Text(text = context.getString(R.string.dot_chart))
                    }
                } else {
                    Button(onClick = { shownGraph = 1 }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface)) {
                        Text(text = context.getString(R.string.dot_chart))
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            if (shownGraph == 0) {
                MoodPieChart(
                    moodData = moodData
                )
            } else if (shownGraph == 1) {
                MoodGrid(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    moodData = moodData,
                    sharedPreferencesHelper = sharedPreferencesHelper
                )
            }
        }
    }
}

@Composable
fun MoodGrid(modifier: Modifier, moodData: List<Pair<LocalDate, Emotion>>, sharedPreferencesHelper: SharedPreferencesHelper) {
    val grid = Array(12) { Array(31) { Color.Transparent } }

    moodData.forEach { (date, emotion) ->
        val month = date.monthValue - 1
        val day = date.dayOfMonth - 1
        grid[month][day] = emotion.color
    }

    Column(modifier = modifier) {
        Row (
            modifier = Modifier,
        ){
            Spacer(modifier = Modifier.width(16.dp))
            repeat(12) { month ->
                Text(
                    modifier = Modifier.width(16.dp),
                    text = getMonthInitial(month+1, sharedPreferencesHelper),
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
        for (day in 0..30) {
            Row (
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    modifier = Modifier.size(16.dp),
                    text = (day+1).toString(),
                    fontSize = 6.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                for (month in 0..11) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(grid[month][day])
                            .border(1.dp, MaterialTheme.colorScheme.onBackground),
                        content = {}
                    )
                }
            }
        }
    }
}

@Composable
fun MoodPieChart(moodData: List<Pair<LocalDate, Emotion>>) {
    val data = moodData.groupBy { it.second }.mapValues { it.value.size }
    PieChart(data = data)
}

fun getMonthInitial(monthNumber: Int, sharedPreferencesHelper: SharedPreferencesHelper): String {
    val localeStr = sharedPreferencesHelper.getSharedPreferencesLocale(SharedPrefsConstants.LANGUAGE)
    val month = Month.of(monthNumber)
    val monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale(localeStr))
    return monthName[0].uppercase()
}

@Preview(showBackground = true)
@Composable
fun YearlyStatsPreview() {
    val mockSharedPreferences = MockSharedPreferences()
    val sharedPreferencesHelper = SharedPreferencesHelper(mockSharedPreferences)

    MoodRhythmTheme {
        YearlyStatsScreen(YearlyStatsActivity(), sharedPreferencesHelper)
    }
}