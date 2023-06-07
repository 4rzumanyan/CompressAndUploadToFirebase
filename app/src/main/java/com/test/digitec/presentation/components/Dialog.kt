package com.test.digitec.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.test.digitec.R
import com.test.digitec.ui.theme.Mulberry
import com.test.digitec.ui.theme.Watermelon

@OptIn(ExperimentalTextApi::class)
@Composable
fun Dialog(
    @StringRes descriptionText: Int,
    @StringRes enableButtonText: Int = R.string.ok,
    @StringRes disableButtonText: Int = R.string.ok,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    action: (Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog)
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside
            )
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.3f)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(30.dp)
            ) {
                Text(
                    text = stringResource(id = descriptionText),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Mulberry,
                                Watermelon
                            )
                        )
                    ),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                GradientButtonRound(
                    modifier = Modifier,
                    enableText = enableButtonText,
                    disableText = disableButtonText,
                    enableColors = listOf(Mulberry, Watermelon),
                    widthFraction = 1f,
                    paddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    isStatic = true
                ) {
                    showDialog = false
                    action.invoke(true)
                }
            }
        }
}
