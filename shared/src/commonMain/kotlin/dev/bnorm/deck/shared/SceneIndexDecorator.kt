package dev.bnorm.deck.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import dev.bnorm.storyboard.LocalSceneMode
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.SceneMode
import dev.bnorm.storyboard.easel.StoryState

fun SceneIndexDecorator(state: StoryState): SceneDecorator = SceneDecorator { content ->
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        content()
        SceneIndex(state, MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onBackground))
    }
}

@Composable
private fun SceneIndex(state: StoryState, style: TextStyle) {
    if (LocalSceneMode.current == SceneMode.Preview) return

    // TODO how do we make this conditional on it being a practice talk?
    //  - enable/disable via the overlay?
    val index = state.currentIndex
    Text(
        text = "${index.sceneIndex},${index.stateIndex}",
        style = style.copy(style.color.copy(alpha = 0.5f)),
    )
}
