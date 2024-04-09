package dev.bnorm.kc24.elements

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.kc24.template.SLIDE_PADDING
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GradleFile(
    text: AnnotatedString,
    visible: Transition<out Boolean>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(Int.MAX_VALUE),
) {
    val outputOffset by visible.animateDp(transitionSpec = { defaultSpec() }) {
        if (it) 20.dp else 1520.dp
    }

    MacWindow(
        color = Color(0xFF313438),
        modifier = modifier.fillMaxHeight().requiredWidth(1520.dp).offset(x = outputOffset),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // TODO use icon from IntelliJ
                Image(
                    painter = painterResource(DrawableResource("gradle/ICON-GRADLE-ALT_MONO-REV.png")),
                    contentDescription = "",
                    modifier = Modifier.size(36.dp),
                )
                Spacer(Modifier.width(16.dp))
                Text(text = "build.gradle.kts", fontSize = 28.sp, lineHeight = 28.sp)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.surface)
                .padding(SLIDE_PADDING)
                .padding(bottom = 100.dp)
        ) {
            Text(text, modifier = Modifier)
        }
    }
}
