package dev.bnorm.evolved.sections.intro

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.Overview() {
    // Spock: https://code.google.com/archive/p/spock/issues/16
    // Groovy: https://dontmindthelanguage.wordpress.com/2009/12/11/groovy-1-7-power-assert/
    // JS: https://github.com/power-assert-js
    // Rust: https://github.com/gifnksm/power-assert-rs
    // Swift: https://github.com/kishikawakatsumi/swift-power-assert
    // Kotlin: https://kotlinlang.org/docs/power-assert.html

    scene(
        stateCount = 9,
//        enterTransition = enter(end = SlideEnter(alignment = Alignment.CenterEnd)),
//        exitTransition = exit(end = SlideExit(alignment = Alignment.CenterEnd)),
    ) {
        HeaderAndBody {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h5) {
                    val reveal = frame.createChildTransition { it.toState() - 1 }
                    RevealEach(reveal) {
                        item { Text("$BULLET_1 First seen in Groovy's Spock test framework as early as 2009") }
                        item { Text("$BULLET_1 Introduced into into the language with Groovy 1.7") }
                        item {
                            Text(
                                text = """
                                    assert 91 == a * b
                                              |  | | |
                                              |  10| 9
                                              |    90
                                              false
                                """.trimIndent(),
                                fontFamily = JetBrainsMono,
                                modifier = Modifier.Companion.padding(start = 64.dp, top = 8.dp, bottom = 8.dp),
                            )
                        }
                        item { Text("$BULLET_1 Now found in numerous other languages (via libraries):") }
                        item { Text("    $BULLET_3 JavaScript") }
                        item { Text("    $BULLET_3 Rust") }
                        item { Text("    $BULLET_3 Swift") }
                        item { Text("    $BULLET_3 And Kotlin!") }
                    }
                }
            }
        }
    }
}
