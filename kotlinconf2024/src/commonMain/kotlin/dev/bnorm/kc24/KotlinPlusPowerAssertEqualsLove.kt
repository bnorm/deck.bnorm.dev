package dev.bnorm.kc24

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.sections.*
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.show.ShowBuilder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove(
    state: AssertionLibrariesState = AssertionLibrariesState(),
) {
    slide { Title() }
    GoodAssertions()
    AssertionLibraries(state)
    WhatIsPowerAssert()
    PowerAssertSample()
    PowerAssertSetup()
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Title() {
    TitleSlide {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Kotlin + Power-Assert = ")
            Image(
                painter = painterResource(DrawableResource("kotlin_mascot/emoji/kodee-loving.png")),
                contentDescription = "",
                modifier = Modifier.requiredSize(100.dp).graphicsLayer { rotationY = 180f },
            )
        }
    }
}
