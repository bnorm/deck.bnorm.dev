package dev.bnorm.kc24.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.section.LocalSlideSection

@Composable
fun TitleAndBody(
    title: @Composable () -> Unit = LocalSlideSection.current.header,
    kodee: KodeeScope.() -> Unit = {},
    body: @Composable () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 32.dp, bottom = 8.dp)
                .requiredHeight(72.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            ProvideTextStyle(MaterialTheme.typography.h3) {
                title()
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(2.dp).background(Color(0xFF7F52FF)))
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(32.dp),
            contentAlignment = Alignment.TopStart,
        ) {
            ProvideTextStyle(MaterialTheme.typography.body1) {
                Column {
                    body()
                }
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().requiredHeight(2.dp).background(Color(0xFF7F52FF)))
    }
    Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.BottomEnd) {
        AnimateKodee { kodee() }
    }
}
