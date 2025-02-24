package dev.bnorm.kc25.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.SlideScope
import dev.bnorm.storyboard.easel.SlideSection

private object SharedHeaderKey

@Composable
fun SlideScope<*>.Header(
    textStyle: TextStyle = MaterialTheme.typography.h3,
    title: @Composable () -> Unit = SlideSection.title,
) {
    Column(
        modifier = Modifier.sharedElement(
            rememberSharedContentState(key = SharedHeaderKey),
            animatedVisibilityScope = this,
        )
    ) {
        Box(Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)) {
            ProvideTextStyle(textStyle) {
                title()
            }
        }
        Spacer(Modifier.fillMaxWidth().requiredHeight(2.dp).background(MaterialTheme.colors.primary))
    }
}
