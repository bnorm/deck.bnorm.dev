package dev.bnorm.dcnyc25

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

class CodeString(
    initialText: String,
) {
    var text: AnnotatedString by mutableStateOf(initialText.highlight(INTELLIJ_LIGHT, language = Language.Kotlin))
        private set

    fun update(newText: String) {
        text = newText.highlight(INTELLIJ_LIGHT, language = Language.Kotlin)
    }
}