package dev.bnorm.kc24.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.section.LocalSlideSection

@Composable
fun TitleAndBody(
    title: @Composable () -> Unit = LocalSlideSection.current.header,
    kodee: KodeeScope.() -> Unit = {},
    body: @Composable BoxScope.() -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = SLIDE_PADDING, vertical = 32.dp)
                .requiredHeight(130.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            ProvideTextStyle(MaterialTheme.typography.h3) {
                title()
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            ProvideTextStyle(MaterialTheme.typography.body1) {
                body()
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
    }
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.BottomEnd) {
        AnimateKodee { kodee() }
    }
}
