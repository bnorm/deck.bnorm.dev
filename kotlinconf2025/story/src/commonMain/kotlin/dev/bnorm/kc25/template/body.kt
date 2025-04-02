package dev.bnorm.kc25.template

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Body(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize().padding(top = 32.dp, start = 32.dp, end = 32.dp),
    ) {
        ProvideTextStyle(MaterialTheme.typography.body1) {
            content()
        }
    }
}
