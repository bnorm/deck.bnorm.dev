package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.SceneHalfHeight
import dev.bnorm.dcnyc25.template.SceneHalfWidth
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

fun StoryboardBuilder.Words() {
    val start = """
        fun main() {
            println("Hello, kotlinconf!")
        }
    """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin)

    val end = """
        fun main() {
            println("Hello, droidcon!")
        }
    """.trimIndent().highlight(INTELLIJ_LIGHT, Language.Kotlin)

    scene(
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Row {
            Vertical(MaterialTheme.colors.primary) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(32.dp),
                ) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Start", style = MaterialTheme.typography.h3)
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = start,
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
            Vertical(MaterialTheme.colors.secondary) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(32.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("End", style = MaterialTheme.typography.h3)
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = end,
                            style = MaterialTheme.typography.code1,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Full(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneHalfWidth * 2).height(SceneHalfHeight * 2), color = color) {
        content()
    }
}

@Composable
fun Vertical(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneHalfWidth).height(SceneHalfHeight * 2), color = color) {
        content()
    }
}

@Composable
fun Horizontal(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneHalfWidth * 2).height(SceneHalfHeight), color = color) {
        content()
    }
}

@Composable
fun Quarter(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.width(SceneHalfWidth).height(SceneHalfHeight), color = color) {
        content()
    }
}
