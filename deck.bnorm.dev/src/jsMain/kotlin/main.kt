import dev.bnorm.deck.DeckStyleSheet
import dev.bnorm.deck.Talks
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Style(DeckStyleSheet)

        Talks()
    }
}

