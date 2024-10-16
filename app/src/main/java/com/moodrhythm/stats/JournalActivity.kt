package com.moodrhythm.stats

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.moodrhythm.R
import com.moodrhythm.ui.theme.MoodRhythmTheme
import com.moodrhythm.utils.CustomAppBar
import com.moodrhythm.utils.MockSharedPreferences
import com.moodrhythm.utils.SharedPrefsConstants.JOURNAL_TEXT
import com.moodrhythm.utils.SharedPrefsHelper
import com.moodrhythm.utils.SharedPrefsHelperImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class JournalActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodRhythmTheme {
                JournalScreen(this, sharedPreferencesHelper)
            }
        }
    }
}

@Composable
fun JournalScreen(activity: Activity, sharedPreferencesHelper: SharedPrefsHelper) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (Modifier.padding(innerPadding)) {
            CustomAppBar(
                onBackClick = {
                    val intent = Intent(activity, StatsActivity::class.java)
                    activity.startActivity(intent)
                }
            )
            JournalContent(sharedPreferencesHelper)
        }
    }
}

@Composable
fun JournalContent(sharedPreferencesHelper: SharedPrefsHelper) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var journalText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        journalText = sharedPreferencesHelper.getSharedPreferencesValueString(JOURNAL_TEXT)
    }

    Column(modifier = Modifier
        .imePadding()
        .fillMaxSize()
        .padding(16.dp)
    ) {
        TextField(
            value = journalText,
            onValueChange = { journalText = it },
            modifier = Modifier.fillMaxWidth().weight(1f),
            label = { Text(getString(context, R.string.write_your_thoughts)) }
        )

        Button(
            onClick = {
                sharedPreferencesHelper.setSharedPreferencesValueString(JOURNAL_TEXT, journalText)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(getString(context, R.string.saved))
                }
            },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(getString(context, R.string.save))
        }

        SnackbarHost(snackbarHostState)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MoodRhythmTheme {
        val mockSharedPreferences = MockSharedPreferences()
        val sharedPreferencesHelper = SharedPrefsHelperImpl(mockSharedPreferences)
        JournalScreen(JournalActivity(), sharedPreferencesHelper)
    }
}