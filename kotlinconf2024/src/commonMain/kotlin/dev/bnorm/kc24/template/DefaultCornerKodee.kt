package dev.bnorm.kc24.template

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.image.Kodee
import dev.bnorm.kc24.image.kodee.Petite

@Composable
fun DefaultCornerKodee() {
    Image(
        painter = rememberVectorPainter(Kodee.Petite),
        contentDescription = "",
        modifier = Modifier.padding(8.dp).requiredSize(73.dp, 63.dp),
    )
}
