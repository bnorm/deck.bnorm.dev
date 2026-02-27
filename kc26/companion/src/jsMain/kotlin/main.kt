import dev.bnorm.kc26.App
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.renderComposableInBody

fun main() {
    renderComposableInBody {
        Style(AppStyleSheet)
        App()
    }
}

private object AppStyleSheet : StyleSheet() {
    init {
        "body" style {
            backgroundColor(Color("#121212"))

            color(Color.white)
            letterSpacing("normal")
            fontFamily("Inter", "sans-serif")
            property("font-optical-sizing", "auto")
        }

        universal style {
            margin(0.px)
        }
    }
}
