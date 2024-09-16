package com.moodrhythm.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.moodrhythm.R

@Composable
fun CustomAppBar(
    icon: Int? = null,
    onIconClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Box(modifier = Modifier.fillMaxWidth().background(color = backgroundColor)) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            onBackClick?.let {
                IconButton(onClick = { onBackClick.invoke() }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_back), contentDescription = null, tint = contentColor)
                }
            }

            Text(
                text = LocalContext.current.getString(R.string.app_name),
                color = contentColor,
                modifier = Modifier.padding(16.dp).weight(1f)
            )

            icon?.let {
                IconButton(onClick = { onIconClick?.invoke() }) {
                    Icon(imageVector = ImageVector.vectorResource(icon), contentDescription = null, tint = contentColor)
                }
            }
        }
    }
}