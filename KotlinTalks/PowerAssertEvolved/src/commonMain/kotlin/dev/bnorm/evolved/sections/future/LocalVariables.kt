package dev.bnorm.evolved.sections.future

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminalPopup
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.evolved.template.code.padLines
import dev.bnorm.evolved.template.code.twice
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.toInt
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

fun StoryboardBuilder.LocalVariables() {
    scene(
        stateCount = 5,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("Local Variables?")
            }
            Box(Modifier.fillMaxSize()) {
                Box(Modifier.padding(horizontal = 32.dp)) {
                    frame.createChildTransition { it.toState() }
                        .MagicCode(
                            LOCAL_VARIABLE_TRANSFORMATIONS,
                            identifierType = { highlighting, identifier ->
                                when (identifier) {
                                    "test" -> highlighting.functionDeclaration
                                    "powerAssert" -> highlighting.staticFunctionCall
                                    else -> null
                                }
                            }
                        )
                }
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    MacTerminalPopup(visible = { it.toInt() in 3..4 }) {
                        if (currentState <= 3) {
                            Text(
                                """
                                    Assertion failed:
                                    val expected = "Hello".length
                                                           |
                                                           5
                                    val actual = "World".substring(1, 4).length
                                                         |               |
                                                         orl             3
                                    powerAssert(expected == actual)
                                                |        |  |
                                                5        |  3
                                                         false
                                """.trimIndent().padLines(12)
                            )
                        } else {
                            Text(
                                """
                                    Assertion failed: 
                                    powerAssert(expected == actual)
                                                |        |  |
                                                |        |  "World".substring(1, 4).length
                                                |        false      |               |
                                                "Hello".length      orl             3
                                                        |
                                                        5
                                """.trimIndent().padLines(12)
                            )
                        }
                    }
                }
            }
        }
    }
}

private val LOCAL_VARIABLE_TRANSFORMATIONS = buildList {
    add(
        """
            @Test fun test() {
                powerAssert(<m>"Hello".length</m=1> == <m>"World".substring(1, 4).length</m=2>)
            }
        """.trimIndent() to """
            @Test fun test() {
            <i>    val expected = </i><m>"Hello".length</m=1>
            <i>    val actual = </i><m>"World".substring(1, 4).length</m=2>
                powerAssert(<i>expected</i> == <i>actual</i>)
            }
        """.trimIndent()
    )

    add(
        """
            @Test fun test() {
                <i></i>val expected = "Hello".length
                <i></i>val actual = "World".substring(1, 4).length
                powerAssert(expected == actual)
            }
        """.trimIndent() to """
            @Test fun test() {
                <i>@Explain </i>val expected = "Hello".length
                <i>@Explain </i>val actual = "World".substring(1, 4).length
                powerAssert(expected == actual)
            }
        """.trimIndent()
    )

    add(
        """
            @Test fun test() {
                @Explain val expected = "Hello".length
                @Explain val actual = "World".substring(1, 4).length
                powerAssert(expected == actual)
            }
        """.trimIndent().twice()
    )
}
