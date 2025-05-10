package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.Pane
import dev.bnorm.dcnyc25.template.Panes
import dev.bnorm.dcnyc25.template.VerticalPane
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

fun StoryboardBuilder.CodeExamples() {
    val panes = listOf<Pane.Vertical<Int>>(
        VerticalPane {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
                Column {
                    Box(Modifier.padding(32.dp)) {
                        Text("Hello World!")
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp).fillMaxSize()
                    ) {
                        Text(
                            text = """
                                fun main() {
                                }
                            """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin),
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        },
        VerticalPane {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
                Column {
                    Box(Modifier.padding(32.dp)) {
                        Text("Hello World!")
                    }
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp).fillMaxSize()
                    ) {
                        Text(
                            text = """
                                fun main() {
                                  println("Hello, World!")
                                }
                            """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin),
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        },
        VerticalPane {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
                Column {
                    Box(Modifier.padding(32.dp)) {
                        Text("Hello World!")
                    }
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp).fillMaxSize()
                    ) {
                        Text(
                            text = """
                                fun main() {
                                  println("Hello, droidcon NYC!")
                                }
                            """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin),
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    )

    scene(
        stateCount = panes.size - 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Panes(panes)
    }
}