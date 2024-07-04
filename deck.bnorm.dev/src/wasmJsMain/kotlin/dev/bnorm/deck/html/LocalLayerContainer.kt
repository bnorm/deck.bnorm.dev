package dev.bnorm.deck.html

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.browser.document
import org.w3c.dom.Element

val LocalLayerContainer = staticCompositionLocalOf<Element> {
    document.body!!
}
