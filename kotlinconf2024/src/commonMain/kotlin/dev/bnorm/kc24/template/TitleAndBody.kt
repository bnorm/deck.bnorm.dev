package dev.bnorm.kc24.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.SlideSection

@Composable
fun SlideScope<*>.TitleAndBody(
    title: @Composable () -> Unit = SlideSection.header,
    kodee: KodeeScope.() -> Unit = {},
    body: @Composable () -> Unit = {},
) {

    Column(Modifier.fillMaxSize()) {
        SharedHeader(MaterialTheme.typography.h3) {
            title()
        }
        Box(Modifier.fillMaxSize()) {
            ProvideTextStyle(MaterialTheme.typography.body1) {
                body()
            }
        }
    }
    SharedKodee {
        AnimateKodee { kodee() }
    }
}

@Composable
fun SlideScope<*>.SharedHeader(textStyle: TextStyle, title: @Composable () -> Unit) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier.sharedElement(
                rememberSharedContentState(key = "title"),
                animatedVisibilityScope = animatedContentScope,
            )
        ) {
            Box(Modifier.fillMaxWidth().padding(horizontal = SLIDE_PADDING, vertical = SLIDE_CONTENT_SPACING)) {
                ProvideTextStyle(textStyle) {
                    title()
                }
            }
            Spacer(Modifier.fillMaxWidth().requiredHeight(4.dp).background(MaterialTheme.colors.primary))
        }
    }
}
