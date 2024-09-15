package com.moodrhythm.model

import androidx.compose.ui.graphics.Color
import com.moodrhythm.R
import com.moodrhythm.ui.theme.*

data class Emotion(
    val id: Int,
    val name: Int,
    val color: Color,
    val textColor: Color,
    val imageId: Int,
    val whiteImageId: Int,
    val moodPlaylist: String
)

// Predefined list of emotions
val emotions = listOf(
    Emotion(0, R.string.happy, HappyColor, Color.Black, R.drawable.happy_icon, R.drawable.happy_icon_white, "https://open.spotify.com/playlist/37i9dQZF1DXdPec7aLTmlC"),
    Emotion(1, R.string.sad, SadColor, Color.White, R.drawable.sad_icon, R.drawable.sad_icon_white, "https://open.spotify.com/playlist/37i9dQZF1DX3YSRoSdA634"),
    Emotion(2, R.string.calm, CalmColor, Color.Black, R.drawable.calm_icon, R.drawable.calm_icon_white, "https://open.spotify.com/playlist/37i9dQZF1DX4sWSpwq3LiO"),
    Emotion(3, R.string.anxious, AnxiousColor, Color.White, R.drawable.anxious_icon, R.drawable.anxious_icon_white, "https://open.spotify.com/playlist/37i9dQZF1DX7gIoKXt0gmx"),
    Emotion(4, R.string.excited, ExcitedColor, Color.White, R.drawable.excited_icon, R.drawable.excited_icon_white, "https://open.spotify.com/playlist/37i9dQZF1DWUVpAXiEPK8P"),
    Emotion(5, R.string.tired, TiredColor, Color.White, R.drawable.tired_icon, R.drawable.tired_icon_white, "https://open.spotify.com/playlist/37i9dQZF1DWZd79rJ6a7lp")
)

fun findEmotionById(emotionId: Int): Emotion {
    return emotions.find { it.id == emotionId } ?: emotions[0]
}