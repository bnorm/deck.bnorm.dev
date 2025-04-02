package dev.bnorm.evolved.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.AnimateKodee
import dev.bnorm.deck.shared.DefaultCornerKodee
import dev.bnorm.deck.shared.KodeeScope
import dev.bnorm.deck.shared.SharedKodee

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun HeaderAndBody(
    kodee: KodeeScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    CornerKodee(kodee) {
        Column(Modifier.fillMaxSize()) {
            Header()
            Body(modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp).fillMaxSize(), {
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
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
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
