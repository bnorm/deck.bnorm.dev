package dev.bnorm.kc26.template

import androidx.compose.runtime.*

// TODO cleanup on aisle section!

private val LocalSceneTitle = compositionLocalOf<SceneTitle?> { null }

@Immutable
class SceneTitle(
    val title: String,
) {
    companion object {
        val current: SceneTitle?
            @Composable
            get() = LocalSceneTitle.current
    }
}

