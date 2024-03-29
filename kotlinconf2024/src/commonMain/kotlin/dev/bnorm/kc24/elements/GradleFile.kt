package dev.bnorm.kc24.elements

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.sections.Image
import dev.bnorm.kc24.template.SLIDE_PADDING

@Composable
fun GradleFile(
    text: AnnotatedString,
    visible: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    val outputOffset by visible.animateDp(transitionSpec = { defaultSpec() }) {
        if (it) 50.dp else 1550.dp
    }

    MacWindow(
        color = Color(0xFF313438),
        modifier = modifier.fillMaxHeight().requiredWidth(1550.dp).offset(x = outputOffset),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // TODO use icon from IntelliJ
                Image("gradle/ICON-GRADLE-ALT_MONO-REV.png", Modifier.size(36.dp))
                Spacer(Modifier.width(16.dp))
                Text(text = "build.gradle.kts", fontSize = 28.sp, lineHeight = 28.sp)
            }
        },
    ) {
        // TODO allow animating the scroll?
        val state = rememberScrollState(Int.MAX_VALUE)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state)
                .background(MaterialTheme.colors.surface)
                .padding(SLIDE_PADDING)
                .padding(bottom = 100.dp)
        ) {
            Text(text, modifier = Modifier)
        }
    }
}
