package dev.bnorm.kc24.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.SlideSection

@Composable
fun TitleAndBody(
    title: @Composable () -> Unit = SlideSection.header,
    kodee: KodeeScope.() -> Unit = {},
    body: @Composable () -> Unit = {},
) {
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().padding(horizontal = SLIDE_PADDING, vertical = SLIDE_CONTENT_SPACING)) {
            ProvideTextStyle(MaterialTheme.typography.h3) {
                title()
            }
        }
        Spacer(Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        Box(Modifier.fillMaxWidth().weight(1f)) {
            ProvideTextStyle(MaterialTheme.typography.body1) {
                body()
            }
        }
        Spacer(Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
    }
    Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.BottomEnd) {
        AnimateKodee { kodee() }
    }
}
