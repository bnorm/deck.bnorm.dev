package dev.bnorm.kc24.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.AnimateKodee
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.KodeeScope
import dev.bnorm.deck.shared.SharedKodee
import dev.bnorm.storyboard.easel.SceneSection
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun TitleAndBody(
    title: @Composable () -> Unit = SceneSection.title,
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
        AnimateKodee {
            default { DefaultCornerKodee(Modifier.size(100.dp)) }
            kodee()
        }
    }
}

private object SharedHeaderKey

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun SharedHeader(textStyle: TextStyle, title: @Composable () -> Unit) {
    Column(
        modifier = Modifier.sharedElement(
            rememberSharedContentState(key = SharedHeaderKey),
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
