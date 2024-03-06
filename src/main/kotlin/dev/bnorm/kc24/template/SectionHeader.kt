package dev.bnorm.kc24.template

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.PreviewSlide
import dev.bnorm.kc24.image.Kodee
import dev.bnorm.kc24.image.kodee.Sitting
import dev.bnorm.librettist.section.LocalSlideSection

@Composable
fun SectionHeader(
    title: @Composable () -> Unit = LocalSlideSection.current.header,
) {
    Box(
        modifier = Modifier.absoluteOffset(56.dp, 225.dp).requiredSize(702.dp, 85.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        ProvideTextStyle(MaterialTheme.typography.h2) {
            title()
        }
    }
    Box(
        modifier = Modifier.absoluteOffset(742.dp, 182.dp).requiredSize(258.dp, 258.dp),
    ) {
        Image(
            imageVector = Kodee.Sitting,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
        )
    }
    Box(
        modifier = Modifier.absoluteOffset(0.dp, 327.dp).requiredSize(1000.dp, 2.dp).background(Color(0xFF7F52FF)),
    )
}

@Preview
@Composable
fun ExampleSectionHeader() {
    PreviewSlide {
        SectionHeader(
            title = { Text(text = "Click to add title") },
        )
    }
}
