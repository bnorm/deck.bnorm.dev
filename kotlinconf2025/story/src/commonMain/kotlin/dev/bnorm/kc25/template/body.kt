package dev.bnorm.kc25.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun HeaderAndBody(
    kodee: KodeeScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    CornerKodee(kodee) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            ProvideTextStyle(MaterialTheme.typography.h3) {
                val section = SceneSection.current
                Header(
                    title = section.title,
                    modifier = Modifier.sharedElement(rememberSharedContentState(key = section))
                )
            }
            Body(modifier.fillMaxSize(), {
                content()
            })
        }
    }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProvideTextStyle(MaterialTheme.typography.body1) {
            content()
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun CornerKodee(
    kodee: KodeeScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        content()
    }

    SharedKodee {
        AnimateKodee {
            default { DefaultCornerKodee(Modifier.size(50.dp)) }
            kodee()
        }
    }
}
