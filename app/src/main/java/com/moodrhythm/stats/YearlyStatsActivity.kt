package com.moodrhythm.stats

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.getCurrentYear
import com.moodrhythm.utils.getSharedPreferencesLocale
import com.moodrhythm.utils.getYearlyMoodData
import java.time.Month
import java.util.Locale

class YearlyStatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                YearlyStatsScreen(this)
            }
        }
    }
}

@Composable
fun YearlyStatsScreen(activity: Activity) {
    Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            CustomAppBar(
                onBackClick = {
                    val intent = Intent(activity, StatsActivity::class.java)
                    activity.startActivity(intent)
                }
            )
            Text(
                text = getCurrentYear().toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
            )
            MoodGrid(
                context = LocalContext.current,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun MoodGrid(context: Context, modifier: Modifier) {
    val moodData = getYearlyMoodData(context)
    val grid = Array(12) { Array(31) { Color.Transparent } }

    moodData.forEach { (date, emotion) ->
        val month = date.monthValue - 1
        val day = date.dayOfMonth - 1
        grid[month][day] = emotion.color
    }

    LazyColumn(modifier = modifier) {
        item {
            Row (
                modifier = Modifier,
            ){
                Spacer(modifier = Modifier.width(16.dp))
                repeat(12) { month ->
                    Text(
                        modifier = Modifier.width(16.dp),
                        text = getMonthInitial(month+1, LocalContext.current),
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        items(31) { day ->
            LazyRow (
                verticalAlignment = Alignment.Top,
            ) {
                item {
                    Text(
                        modifier = Modifier.size(16.dp),
                        text = (day+1).toString(),
                        fontSize = 6.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
                items(12) { month ->
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

fun getMonthInitial(monthNumber: Int, context: Context): String {
    val localeStr = getSharedPreferencesLocale(context, SharedPrefsConstants.LANGUAGE)
    val month = Month.of(monthNumber)
    val monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale(localeStr))
    return monthName[0].uppercase()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MoodRhythmTheme {
        YearlyStatsScreen(YearlyStatsActivity())
    }
}