package dev.bnorm.kc24.elements

import androidx.compose.animation.core.Transition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.generated.resources.Res
import dev.bnorm.deck.shared.generated.resources.kotlinGradleScript_dark
import dev.bnorm.kc24.template.SLIDE_PADDING
import org.jetbrains.compose.resources.painterResource

@Composable
fun GradleFile(
    text: AnnotatedString,
    visible: Transition<out Boolean>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(Int.MAX_VALUE),
) {
    visible.AnimatedVisibility(
        modifier = modifier.fillMaxHeight().requiredWidth(1520.dp).offset(x = 20.dp),
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it },
    ) {
        MacWindow(
            color = Color(0xFF313438),
            modifier = modifier.fillMaxSize(),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // TODO use icon from IntelliJ
                    Image(
                        painter = painterResource(Res.drawable.kotlinGradleScript_dark),
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
                    .verticalScroll(scrollState, enabled = false)
                    .background(MaterialTheme.colors.surface)
                    .padding(SLIDE_PADDING)
                    .padding(bottom = 100.dp)
            ) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    Text(text, modifier = Modifier)
                }
            }
        }
    }
}
