package dev.bnorm.kc24

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.typingSpec
import dev.bnorm.kc24.examples.*
import dev.bnorm.kc24.sections.Future
import dev.bnorm.kc24.template.KodeeLoving
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import dev.bnorm.librettist.show.section
import dev.bnorm.librettist.text.thenLineEndDiff
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove() {
    val section1 = "Crafting an Assertion"
    val section2 = "Why Power-Assert?"
    val section3 = "A Look at the Future"

    slide { Title() }

    section(title = section1) {
        SectionHeader(animateToBody = true)
        ExampleCarousel(
            start = { },
            end = { Example(BasicAssertTrueCode) }
        )
        BasicAssertTrueExample()
        ExampleTransition {
            val start = BasicAssertTrueCode
            val end = BasicAssertEqualsCode
            remember { startAnimation(start).thenLineEndDiff(end).toList() }
        }
        BasicAssertEqualsExample()
        ExampleTransition {
            val start = BasicAssertEqualsCode
            val end = AssertEqualsMessageCode
            remember { startAnimation(start).thenLineEndDiff(end).toList() }
        }
        AssertEqualsMessageExample()
        ExampleTransition {
            val start = AssertEqualsMessageCode
            val end = AssertKCode
            remember { startAnimation(start).thenLineEndDiff(end).toList() }
        }
        AssertKExample()
        SectionHeader(animateFromBody = true)
    }

    // TODO transition from bike-shedding to a new power-assert library seems counter productive

    SectionChange(section1, section2)

    section(title = section2) {
        SectionHeader(animateToBody = true)
        ExampleCarousel(
            start = { },
            end = { Example(SimpleAssertCode) }
        )
        SimpleAssertExample()
        ExampleCarousel(
            start = { Example(SimpleAssertCode) },
            end = { Example(ComplexAssertCode) }
        )
        ComplexExpressionsExample()
        ExampleCarousel(
            start = { Example(ComplexAssertCode) },
            end = { Example(AssertTrueSmartcastCode) }
        )
        AssertTrueSmartcastExample()
        ExampleCarousel(
            start = { Example(AssertTrueSmartcastCode) },
            end = { Example(RequireCode) }
        )
        RequireExample()
        ExampleCarousel(
            start = { Example(RequireCode) },
            end = { Example(AssertEqualsCode) }
        )
        AssertEqualsAndNotNullExample()
        // TODO examples
        //  - at this point we could compare assertTrue/assertEquals/assertNotNull as the primary toolbox

        // TODO summary slide before going into the next example?
        ExampleCarousel(
            start = { Example(AssertEqualsAndNotNullCode) },
            end = { Example(SoftAssertWithoutMessageSetup) }
        )
        SoftAssertExample()
        SectionHeader(animateFromBody = true)
    }

    SectionChange(section2, section3)

    section(title = section3) {
        SectionHeader(animateToBody = true)
        Future()
    }

    slide { Summary() }
}

private fun ShowBuilder.SectionChange(previousTitle: String, nextTitle: String) {
    slide(states = 0) {
        SectionHeader(showAsBody = updateTransition(false)) {
            val values = remember(previousTitle, nextTitle) {
                startAnimation(previousTitle).thenLineEndDiff(nextTitle).toList()
            }
            val text by transition.animateList(values, transitionSpec = { typingSpec(count = values.size) }) {
                if (it == SlideState.Exiting) values.lastIndex else 0
            }
            Text(text)
        }
    }
}

@Composable
fun Title() {
    TitleSlide {
        Box(Modifier.fillMaxSize()) {
            // TODO add some animation?
            //  - make image slide to the left when exiting?
            //  - drop title off the bottom
            //  - how does the next slide appear?
            Image(
                painter = painterResource(DrawableResource("opening_background.png")),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )

            Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
                Column(Modifier.fillMaxWidth().align(Alignment.BottomStart), horizontalAlignment = Alignment.Start) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        Text("Kotlin")
                        Text(" + Power-Assert")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(" = ")
                            KodeeLoving(modifier = Modifier.requiredSize(125.dp).graphicsLayer { rotationY = 180f })
                        }
                    }
                    Spacer(Modifier.size(32.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        BrianNorman(modifier = Modifier.align(Alignment.BottomStart))
                        Mastodon(modifier = Modifier.align(Alignment.BottomEnd))
                    }
                }
            }
        }
    }
}

@Composable
fun Summary() {
    TitleSlide {
        Box(Modifier.fillMaxSize()) {
            // TODO add some animation?
            //  - make name tag drop down from the top with a bounce
            //  - make phone slide in from the right
            //  - make arrow animation path

            // TODO add background image as a second state to this slide so the links are the focus
            //  - move "Thank You" to the bottom and include don't forget to vote
            Image(
                painter = painterResource(DrawableResource("closing_background.png")),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )

            Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {

                // TODO can we do something more stylistically interesting with this?
                Column {
                    ProvideTextStyle(MaterialTheme.typography.body1) {
                        // TODO create these links
                        // TODO make these links clickable
                        Text("Docs: kotl.in/power-assert")
                        Text("KotlinLang Slack: #power-assert") // kotl.in/power-assert-slack
                        Text("Slides: deck.bnorm.dev/kotlinconf2024")
                    }
                }

                Text(
                    text = "Thank you,\nand don’t \nforget to vote!",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.align(Alignment.BottomStart),
                )
            }
        }
    }
}

@Composable
private fun BrianNorman(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(DrawableResource("social/JetBrains.png")),
            contentDescription = "",
            modifier = Modifier.height(64.dp),
        )
        Spacer(modifier = Modifier.size(24.dp))
        Column {
            Text("Brian Norman", style = MaterialTheme.typography.h5)
            Text("Kotlin Compiler Developer", style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
private fun Mastodon(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(48.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(DrawableResource("social/mastodon.png")),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = "bnorm@kotlin.social", style = MaterialTheme.typography.body2)
    }
}
