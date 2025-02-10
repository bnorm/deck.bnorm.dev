package dev.bnorm.evolved.template

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
import dev.bnorm.storyboard.core.SlideScope

@Composable
fun SlideScope<*>.HeaderAndBody(
    kodee: KodeeScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Header()

        Column(
            modifier = modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProvideTextStyle(MaterialTheme.typography.body1) {
                content()
            }
        }
    }

    SharedKodee {
        AnimateKodee {
            default { DefaultCornerKodee(Modifier.size(50.dp)) }
            kodee()
        }
    }
}
