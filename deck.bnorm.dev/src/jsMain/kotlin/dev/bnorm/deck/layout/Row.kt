package dev.bnorm.deck.layout

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
fun Row(content: @Composable () -> Unit) {
    Div({
        style {
            display(DisplayStyle.Companion.Flex)
            flexDirection(FlexDirection.Companion.Row)
            alignItems(AlignItems.Companion.Center)
            width(100.percent)
        }
    }) {
        content()
    }
}
