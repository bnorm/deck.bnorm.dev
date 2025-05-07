package dev.bnorm.dcnyc25

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.background_building
import dev.bnorm.deck.story.generated.resources.droidcon_newyork
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import org.jetbrains.compose.resources.painterResource

fun createStoryboard(
    decorator: SceneDecorator = storyDecorator(),
): Storyboard = Storyboard.build(
    title = "(Re)creating Magic(Move) with Compose",
    description = """
        Presentation software is extremely powerful, full of creative styling, powerful animations, and plenty of
        other things. Yet every time I wrote a new presentation, I found myself repeating something with almost
        every slide; whether that be the content, styling, or animations. Wanting the torment to end (and a
        different kind of torment to begin), I wrote my own presentation framework with Compose!

        But wanting to be like all the cool presenters, I needed something like Keynote’s Magic Move for all my code
        examples! Compose has SharedTransitionLayout, how hard could this be? So I built my own version of Magic
        Move. And rebuilt it. And rebuilt it again. And Again. (And probably again between submitting and actually
        giving this talk) Let’s look at all those different iterations, the improvements each brought, and why
        diffing algorithms are hard.
    """.trimIndent(),
    decorator = decorator,
) {
    Title()

    // TODO diff algorithms
    //  * https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore_string-search_algorithm
    //  * https://blog.jcoglan.com/2017/09/19/the-patience-diff-algorithm/

//    scene(
//        enterTransition = enter(
//            start = SceneEnter(alignment = Alignment.BottomCenter),
//            end = SceneEnter(alignment = Alignment.CenterEnd),
//        ),
//        exitTransition = exit(
//            start = SceneExit(alignment = Alignment.BottomCenter),
//            end = SceneExit(alignment = Alignment.CenterEnd),
//        ),
//    ) {
//        Surface(color = MaterialTheme.colors.secondary) {
//            Column {
//                Box(modifier = Modifier.padding(start = 32.dp, top = 16.dp, bottom = 8.dp).fillMaxWidth()) {
//                    Text("Hello World!", style = MaterialTheme.typography.h3)
//                }
//                Box(Modifier.fillMaxWidth().height(2.dp).background(MaterialTheme.colors.primary))
//            }
//        }
//    }
//    scene(
//        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
//        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
//    ) {
//        Surface(color = MaterialTheme.colors.primary, modifier = Modifier.fillMaxSize()) {
//            Box(Modifier.padding(32.dp)) {
//                Text("Hello World!")
//            }
//        }
//    }
}

private fun StoryboardBuilder.Title() {
    scene(
        enterTransition = SceneEnter(alignment = Alignment.BottomCenter),
        exitTransition = SceneExit(alignment = Alignment.BottomCenter),
    ) {
        Column(Modifier.fillMaxSize()) {
            Surface(color = MaterialTheme.colors.primary, modifier = Modifier.weight(1f)) {
                Row {
                    Image(
                        painter = painterResource(Res.drawable.droidcon_newyork),
                        contentDescription = "title",
                        modifier = Modifier.width(LocalStoryboard.current!!.format.toDpSize().width / 2).padding(32.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(Res.drawable.background_building),
                        contentDescription = "building",
                    )
                }
            }
            Surface(color = MaterialTheme.colors.secondary) {
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                                append("re")
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append("creating\nmagic")
                                }
                                append("move\nwith compose")
                            }
                        },
                        style = MaterialTheme.typography.h2,
                    )
                    Spacer(Modifier.weight(1f))
                    Column {
                        JetBrainsEmployee(
                            name = "Brian Norman",
                            title = "Kotlin Compiler Developer",
                        )
                        Spacer(Modifier.size(4.dp))
                        Column(Modifier.padding(start = 12.dp)) {
                            Mastodon(username = "bnorm@kotlin.social")
                            Spacer(Modifier.size(4.dp))
                            Bluesky(username = "@bnorm.dev")
                        }
                    }
                }
            }
        }
    }
}
