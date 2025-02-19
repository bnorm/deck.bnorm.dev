package dev.bnorm.evolved.sections.future

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.evolved.template.code.twice
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.IntelliJ() {
    slide(
        stateCount = 2,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("IntelliJ?")
            }
            Box(Modifier.padding(horizontal = 32.dp)) {
                state.createChildTransition { it.toState() }
                    .MagicCode(INTELLIJ_TRANSFORMATIONS)
            }
        }
    }
}

private val INTELLIJ_TRANSFORMATIONS = buildList {
    add(
        """
            @Test fun test() {
                @Explain val expected = "Hello".length
                @Explain val actual = "World".substring(1, 4).length
                powerAssert(expected == actual)
            }
        """.trimIndent() to """
            @Test fun test() {
                @Explain val expected = "Hello".length
            <i>//                                  |</i>
            <i>//                                  5</i>
                @Explain val actual = "World".substring(1, 4).length
            <i>//                                |               |</i>
            <i>//                                orl             3</i>
                powerAssert(expected == actual)
            <i>//              |        |  |</i>
            <i>//              5        |  3</i>
            <i>//                       false</i>
            }
        """.trimIndent()
    )

    add(
        """
            @Test fun test() {
                @Explain val expected = "Hello".length
            //                                  |
            //                                  5
                @Explain val actual = "World".substring(1, 4).length
            //                                |               |
            //                                orl             3    
                powerAssert(expected == actual)
            //              |        |  |
            //              5        |  3
            //                       false
            }
        """.trimIndent().twice()
    )
}
