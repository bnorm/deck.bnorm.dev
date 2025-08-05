package dev.bnorm.deck

import androidx.compose.runtime.Composable
import dev.bnorm.deck.layout.Column
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.flex
import org.jetbrains.compose.web.dom.Div

@Composable
fun Talks() {
    Column {
        Div({
            style {
                flex("1 0 auto")
                boxSizing("border-box")
            }
        }) {
            Talk("(Re)creating Magic(Move) with Compose", Video.YouTube("PgzBWebeJsk"), "dcnyc25")
            Talk("Writing Your Third Kotlin Compiler Plugin", Video.YouTube("9P7qUGi5_gc"), "kc25")
            Talk("Kotlin + Power-Assert = Love", Video.YouTube("N8u-6d0iCiE"), "kotlinconf2024")
            Talk("Declarative Test Setup", Video.YouTube("_K25Z--4hxg"))
            Talk("Elevated Gardening with the Kotlin Ecosystem", Video.YouTube("nVj9mbWz-Os"))
        }
    }
}
