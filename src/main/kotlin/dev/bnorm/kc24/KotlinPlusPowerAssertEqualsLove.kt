package dev.bnorm.kc24

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.sections.AssertionLibraries
import dev.bnorm.kc24.sections.PowerAssertSetup
import dev.bnorm.kc24.sections.WhyPowerAssert
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.DesktopSlideShow

fun main() {
    DesktopSlideShow(title = "Kotlin + Power-Assert = Love", theme = Theme.dark) {
//        Samples()
        slide { Title() }
        AssertionLibraries()
        WhyPowerAssert()
        PowerAssertSetup()
    }
}

@Composable
fun Title() {
    TitleSlide {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kotlin + Power-Assert = ")
            Image(
                painter = painterResource("kotlin_mascot/emoji/kodee-loving.png"),
                contentDescription = "",
                modifier = Modifier.requiredSize(100.dp),
            )
        }
    }
}
