package dev.bnorm.evolved.template.code

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import dev.bnorm.deck.shared.mac.MacWindow

@Composable
fun KotlinFile(
    text: AnnotatedString,
    title: String,
    visible: Transition<out Boolean>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    visible.AnimatedVisibility(
        visible = { it },
        modifier = modifier.fillMaxHeight().requiredWidth(760.dp).offset(x = 10.dp),
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it },
    ) {
        MacWindow(
            color = Color(0xFF313438),
            modifier = modifier.fillMaxSize(),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // TODO switch to kotlin flag from IntelliJ
//                    Image(
//                        painter = painterResource(Res.drawable.kotlinGradleScript_dark),
//                        contentDescription = "",
//                        modifier = Modifier.size(18.dp),
//                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = title, fontSize = 14.sp, lineHeight = 14.sp)
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(MaterialTheme.colors.surface)
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
            ) {
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    Text(text, modifier = Modifier)
                }
            }
        }
    }
}
