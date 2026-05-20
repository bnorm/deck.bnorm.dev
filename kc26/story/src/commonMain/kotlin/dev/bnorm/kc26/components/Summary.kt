package dev.bnorm.kc26.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun Summary(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        val availableHeight = 540.dp
        val timelineHeight = availableHeight - 64.dp

        val padding = 32.dp
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .height(timelineHeight - 2 * padding)
        ) {
            Box(Modifier.padding(horizontal = 32.dp).padding(top = 24.dp)) {
                ProvideTextStyle(MaterialTheme.typography.body1) {
                    content()
                }
            }
        }
    }
}

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "
