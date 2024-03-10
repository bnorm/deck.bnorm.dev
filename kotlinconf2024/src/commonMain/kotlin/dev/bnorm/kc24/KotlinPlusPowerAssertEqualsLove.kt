package dev.bnorm.kc24

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.sections.*
import dev.bnorm.kc24.template.KodeeLoving
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.show.ShowBuilder

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

@Composable
fun Title() {
    TitleSlide {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Kotlin + Power-Assert = ")
            KodeeLoving(modifier = Modifier.requiredSize(100.dp).graphicsLayer { rotationY = 180f })
        }
    }
}
