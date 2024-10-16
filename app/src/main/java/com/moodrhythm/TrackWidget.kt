package com.moodrhythm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import com.moodrhythm.model.emotions
import com.moodrhythm.model.findEmotionById
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.SharedPrefsHelperImpl

object TrackWidget : GlanceAppWidget() {

    val emotionIdKey = intPreferencesKey("emotionId")
    
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            TrackWidgetContent(context)
        }
    }

    @Composable
    private fun TrackWidgetContent(context: Context) {
        val sharedPrefs = context.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val sharedPreferencesHelper = SharedPrefsHelperImpl(sharedPrefs)
        val currentEmotionId = sharedPreferencesHelper.getSharedPreferencesValueInt(sharedPreferencesHelper.getCurrentDayEmotionIdKey())

        val emotionId = currentState(key = emotionIdKey) ?: currentEmotionId
        Log.d("TrackWidget", "Current emotion id: $emotionId, emotionIdKey: $emotionIdKey, currentEmotionId: $currentEmotionId")

        Row(
            modifier = GlanceModifier.padding(4.dp)
        ) {
            if (emotionId == -1) {
                val halfSize = emotions.size / 2
                Column {
                    emotions.take(halfSize).forEach { emotion ->
                        EmotionButton(context = context, emotionId = emotion.id, emotionName = emotion.name, emotionColor = emotion.color, textColor = emotion.textColor)
                        Spacer(modifier = GlanceModifier.padding(2.dp))
                    }
                }
                Column {
                    emotions.drop(halfSize).forEach { emotion ->
                        EmotionButton(context = context, emotionId = emotion.id, emotionName = emotion.name, emotionColor = emotion.color, textColor = emotion.textColor)
                        Spacer(modifier = GlanceModifier.padding(2.dp))
                    }
                }
            } else {
                Column {
                    Text(
                        text = getString(context, R.string.todays_mood_is) + ": ${getString(context, findEmotionById(emotionId).name)}"
                    )
                    Spacer(modifier = GlanceModifier.padding(4.dp))
                    Button(
                        text = getString(context, R.string.update),
                        onClick = actionRunCallback<UpdateCallback>()
                    )
                }
            }
        }
    }

    @Composable
    private fun EmotionButton(context: Context, emotionId: Int, emotionName: Int, emotionColor: Color, textColor: Color) {
        Button(
            text = getString(context, emotionName),
            onClick = actionRunCallback<EmotionSelectionCallback>(
                actionParametersOf(EmotionSelectionCallback.EMOTION_ID_KEY to emotionId)
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorProvider(emotionColor),
                contentColor = ColorProvider(textColor)
            )
        )
    }
}

class TrackWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TrackWidget
}

class EmotionSelectionCallback : ActionCallback {
    companion object {
        val EMOTION_ID_KEY = ActionParameters.Key<Int>("EMOTION_ID")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val selectedEmotionId = parameters[EMOTION_ID_KEY] ?: return
        val sharedPrefs = context.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val sharedPreferencesHelper = SharedPrefsHelperImpl(sharedPrefs)

        sharedPreferencesHelper.setSharedPreferencesValueInt(
            sharedPreferencesHelper.getCurrentDayEmotionIdKey(),
            selectedEmotionId
        )

        // Update the widget state with the selected emotion
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[TrackWidget.emotionIdKey] = selectedEmotionId
        }

        TrackWidget.update(context, glanceId)
    }
}

class UpdateCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val sharedPrefs = context.getSharedPreferences(SharedPrefsConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val sharedPreferencesHelper = SharedPrefsHelperImpl(sharedPrefs)

        val currentEmotionId = sharedPreferencesHelper.getSharedPreferencesValueInt(sharedPreferencesHelper.getCurrentDayEmotionIdKey())

        // Update the widget state with the selected emotion
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[TrackWidget.emotionIdKey] = currentEmotionId
        }

        TrackWidget.update(context, glanceId)
    }
}
