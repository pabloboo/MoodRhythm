package com.moodrhythm

import androidx.compose.ui.graphics.Color
import com.moodrhythm.ui.theme.*

data class Emotion(
    val id: Int,
    val name: Int,
    val color: Color,
    val imageId: Int,
    val whiteImageId: Int
)

// Predefined list of emotions
val emotions = listOf(
    Emotion(0, R.string.happy, HappyColor, R.drawable.happy_icon, R.drawable.happy_icon_white),
    Emotion(1, R.string.sad, SadColor, R.drawable.sad_icon, R.drawable.sad_icon_white),
    Emotion(2, R.string.calm, CalmColor, R.drawable.calm_icon, R.drawable.calm_icon_white),
    Emotion(3, R.string.anxious, AnxiousColor, R.drawable.anxious_icon, R.drawable.anxious_icon_white),
    Emotion(4, R.string.excited, ExcitedColor, R.drawable.excited_icon, R.drawable.excited_icon_white),
    Emotion(5, R.string.tired, TiredColor, R.drawable.tired_icon, R.drawable.tired_icon_white)
)