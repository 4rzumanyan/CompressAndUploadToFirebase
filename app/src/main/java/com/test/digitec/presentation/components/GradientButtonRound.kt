package com.test.digitec.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun GradientButtonRound(
    modifier: Modifier,
    @StringRes enableText: Int,
    @StringRes disableText: Int,
    enableColors: List<Color>,
    disableColor: Color = Gray.copy(alpha = 0.3f),
    disableColors: List<Color> = listOf(disableColor, disableColor),
    widthFraction: Float,
    paddingValues: PaddingValues,
    isStatic: Boolean = false,
    onClick: () -> Unit
) {
    var enabled by remember {
        mutableStateOf(true)
    }

    Box(
        modifier = modifier
            .fillMaxWidth(fraction = widthFraction)
            .background(
                brush = Brush.horizontalGradient(colors = if (enabled) enableColors else disableColors),
                shape = RoundedCornerShape(percent = 50)
            )
            // To make the ripple round
            .clip(shape = RoundedCornerShape(percent = 50))
            .clickable {
                if (isStatic.not()) enabled = false
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = if (enabled) enableText else disableText),
            fontSize = 24.sp,
            modifier = Modifier.padding(paddingValues),
            fontWeight = FontWeight.Medium,
            color = if (enabled) Color.White else Color.Gray
        )
    }
}