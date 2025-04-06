package dev.bnorm.deck.layout

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Div

@Composable
fun Column(content: @Composable () -> Unit) {
    Div({
        style {
            display(DisplayStyle.Companion.Flex)
            flexDirection(FlexDirection.Companion.Column)
            alignItems(AlignItems.Companion.Center)
            height(100.percent)
        }
    }) {
        content()
    }
}
