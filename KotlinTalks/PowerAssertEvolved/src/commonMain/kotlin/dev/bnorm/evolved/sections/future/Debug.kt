package dev.bnorm.evolved.sections.future

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
import dev.bnorm.evolved.template.code.padLines
import dev.bnorm.evolved.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.core.toInt
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit

fun StoryboardBuilder.Debug() {
    slide(
        stateCount = 2,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("Pretty Print?")
            }
            Box(Modifier.fillMaxSize()) {
                Text(
                    """
                        val str = "World"
                        prettyln(str.substring(1, 4).length)
                    """.trimIndent().toCode(),
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    MacTerminalPopup(visible = { it.toInt() == 1 }) {
                        Text(
                            """
                                prettyln(str.substring(1, 4).length)
                                str = World
                                str.substring(1, 4) = orl
                                str.substring(1, 4).length = 3
                            """.trimIndent().padLines(10),
                        )
                    }
                }
            }
        }
    }
    slide(
        stateCount = 2,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderAndBody {
            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text("Debug Print?")
            }
            Box(Modifier.fillMaxSize()) {
                Text(
                    """
                        if (dbg(str.length > 0)) {
                        }
                    """.trimIndent().toCode(),
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
                ProvideTextStyle(MaterialTheme.typography.body2) {
                    MacTerminalPopup(visible = { it.toInt() == 1 }) {
                        Text(
                            """
                                dbg(str.length > 0)
                                str = World
                                str.length = 5
                                str.length > 0 = true
                            """.trimIndent().padLines(10),
                        )
                    }
                }
            }
        }
    }
}
