package dev.bnorm.deck

import androidx.compose.runtime.Composable
import dev.bnorm.deck.layout.FlexColumn
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

@Composable
fun Talks() {
    // Outer FlexColumn constrains width to min required by children.
    // Inner FlexColumn stretches each child to fill max width.
    FlexColumn(alignItems = AlignItems.Center) {
        FlexColumn(gap = 32.px) {
            Div {} // Header gap hack.
            Talk(
                title = "(Re)creating Magic(Move) with Compose",
                subtitle = "Droidcon NYC 2025",
                video = Video.YouTube("PgzBWebeJsk"),
                storyId = "dcnyc25"
            )
            Talk(
                title = "Writing Your Third Kotlin Compiler Plugin",
                subtitle = "KotlinConf 2025",
                video = Video.YouTube("9P7qUGi5_gc"),
                storyId = "kc25"
            )
            Talk(
                title = "Kotlin + Power-Assert = Love",
                subtitle = "KotlinConf 2024",
                video = Video.YouTube("N8u-6d0iCiE"),
                storyId = "kotlinconf2024"
            )
            Talk(
                title = "Declarative Test Setup",
                subtitle = "KotlinConf 2023",
                video = Video.YouTube("_K25Z--4hxg")
            )
            Talk(
                title = "Elevated Gardening with the Kotlin Ecosystem",
                subtitle = "KotlinConf 2023",
                video = Video.YouTube("nVj9mbWz-Os")
            )
            Div {} // Footer gap hack.
        }
    }
}
