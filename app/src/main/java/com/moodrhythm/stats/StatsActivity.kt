package com.moodrhythm.stats

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moodrhythm.R
import com.moodrhythm.ResultsActivity
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar

class StatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                StatsScreen(this)
            }
        }
    }
}

@Composable
fun StatsScreen(activity: Activity) {
    Scaffold(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            CustomAppBar(
                onBackClick = {
                    val intent = Intent(activity, ResultsActivity::class.java)
                    activity.startActivity(intent)
                }
            )
            StatsTitleCard(
                modifier = Modifier,
                title = R.string.yearly_stats,
                onClick = { yearlyStatsClick(activity) }
            )
        }
    }
}

@Composable
fun StatsTitleCard(modifier: Modifier ,title: Int, onClick: () -> Unit) {
    Card (
        modifier = modifier.padding(vertical = 4.dp, horizontal = 16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick) {
        Text(
            text = LocalContext.current.getString(title),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

fun yearlyStatsClick(activity: Activity) {
    val intent = Intent(activity, YearlyStatsActivity::class.java)
    activity.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoodRhythmTheme {
        StatsScreen(StatsActivity())
    }
}