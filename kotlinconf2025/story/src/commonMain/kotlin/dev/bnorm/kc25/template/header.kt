package dev.bnorm.kc25.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Header(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(Modifier.height(16.dp))
        title()
        Spacer(Modifier.height(4.dp))
        Spacer(
            Modifier
                .fillMaxWidth().requiredHeight(2.dp)
                .padding(horizontal = 64.dp)
                .background(MaterialTheme.colors.secondary)
        )
    }
}
