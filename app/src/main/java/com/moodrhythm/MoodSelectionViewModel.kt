package com.moodrhythm

import androidx.lifecycle.ViewModel
import com.moodrhythm.model.Emotion
import com.moodrhythm.model.emotions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MoodSelectionViewModel: ViewModel() {
    private val _selectedEmotion = MutableStateFlow(emotions[0])
    val selectedEmotion: StateFlow<Emotion> = _selectedEmotion

    fun selectEmotion(emotion: Emotion) {
        _selectedEmotion.value = emotion
    }
}