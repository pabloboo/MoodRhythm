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
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar
import com.moodrhythm.utils.getMonthName
import com.moodrhythm.utils.getMonthlyMoodData
import com.moodrhythm.utils.nextMonth
import com.moodrhythm.utils.previousMonth
import java.time.LocalDate
import java.time.Month

class MonthlyStatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                MonthlyStatsScreen(this)
            }
        }
    }
}

@Composable
fun MonthlyStatsScreen(activity: Activity) {
    var month by remember { mutableStateOf(LocalDate.now().month) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (Modifier.padding(innerPadding)) {
            CustomAppBar(
                onBackClick = {
                    val intent = Intent(activity, StatsActivity::class.java)
                    activity.startActivity(intent)
                }
            )
            Row (modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
                IconButton(onClick = { month = previousMonth(month) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left), contentDescription = "Left arrow icon")
                }
                Text(
                    text = getMonthName(activity, month),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp).weight(1f)
                )
                IconButton(onClick = { month = nextMonth(month) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right), contentDescription = "Right arrow icon")
                }
            }
            MoodMonthlyVisualizer(
                context = LocalContext.current,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                month = month
            )
        }
    }
}

@Composable
fun MoodMonthlyVisualizer(context: Context, modifier: Modifier = Modifier, month: Month) {
    val monthlyData = getMonthlyMoodData(context, month)
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
        val week = date.dayOfMonth / 7
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
    MoodRhythmTheme {
        MonthlyStatsScreen(MonthlyStatsActivity())
    }
}