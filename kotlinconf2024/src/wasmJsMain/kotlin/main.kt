import dev.bnorm.kc24.KotlinPlusPowerAssertEqualsLove
import dev.bnorm.kc24.Theme
import dev.bnorm.librettist.WebSlideShow
import dev.bnorm.librettist.show.ShowBuilder

fun main() {
    WebSlideShow(
        canvasElementId = "ComposeTarget",
        theme = { Theme.dark },
        builder = ShowBuilder::KotlinPlusPowerAssertEqualsLove
    )
}
