package dev.bnorm.deck.layout

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun FlexRow(
    gap: CSSNumeric? = null,
    alignItems: AlignItems? = null,
    content: @Composable () -> Unit,
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            width(100.percent)
            if (alignItems != null) alignItems(alignItems)
            if (gap != null) gap(gap)
        }
    }) {
        content()
    }
}

@Composable
fun FlexColumn(
    gap: CSSNumeric? = null,
    alignItems: AlignItems? = null,
    content: @Composable () -> Unit,
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            height(100.percent)
            if (alignItems != null) alignItems(alignItems)
            if (gap != null) gap(gap)
        }
    }) {
        content()
    }
}
