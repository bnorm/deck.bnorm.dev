package dev.bnorm.kc25.template

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
import dev.bnorm.storyboard.core.SlideScope

@Composable
fun SlideScope<*>.HeaderAndBody(
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
                Header()
            }
            Body(modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

@Composable
fun SlideScope<*>.Body(
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
fun SlideScope<*>.CornerKodee(
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
