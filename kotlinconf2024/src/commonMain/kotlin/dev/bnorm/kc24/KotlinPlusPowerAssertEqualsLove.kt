package dev.bnorm.kc24

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import dev.bnorm.kc24.elements.SocialGitHub
import dev.bnorm.kc24.elements.SocialMastodon
import dev.bnorm.kc24.elements.typingSpec
import dev.bnorm.kc24.examples.*
import dev.bnorm.kc24.sections.Future
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.SlideState
import dev.bnorm.librettist.show.section
import dev.bnorm.librettist.text.thenLineEndDiff
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove() {
    slide { Title() }

    // TODO
    //  - make sure the main point here is emphasized!!!
    //  - improve the flow between the section

    // TODO slide transition between sections?

    val section1 = "Crafting an Assertion"
    val section2 = "Why Power-Assert?"
    val section3 = "A Look at the Future"

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

    SectionChange(section1, section2)

    section(title = section2) {
        SectionHeader(animateToBody = true)
        // TODO should each example have conclusions?
        // TODO should each example end with a question that leads into the next example?
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
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Kotlin + Power-Assert = ")
                        KodeeLoving(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
                    }

                    PresenterSocials()
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
            Image(
                painter = painterResource(DrawableResource("closing_background.png")),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )

            Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {

                // TODO can we do something more stylistically interesting with this?
                Column(modifier = Modifier.align(Alignment.CenterStart),) {
                    Text("Thank You!")
                    Spacer(Modifier.requiredHeight(SLIDE_CONTENT_SPACING))
                    ProvideTextStyle(MaterialTheme.typography.body2) {
                        Column {
                            // TODO create these links
                            // TODO make these links clickable
                            Text("Docs: kotl.in/power-assert")
                            Text("Slack: kotl.in/power-assert-slack")
                            Text("Slides: deck.bnorm.dev/kotlinconf2024")
                        }
                    }
                }

                PresenterSocials(Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
private fun PresenterSocials(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().requiredHeight(64.dp)) {
        ProvideTextStyle(MaterialTheme.typography.body2) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                Text("Brian Norman")
            }
            Spacer(modifier = Modifier.requiredWidth(4.dp).fillMaxHeight().background(MaterialTheme.colors.primary))
            SocialMastodon(username = "bnorm@kotlin.social", modifier = Modifier.weight(1.5f).fillMaxHeight())
            Spacer(modifier = Modifier.requiredWidth(4.dp).fillMaxHeight().background(MaterialTheme.colors.primary))
            SocialGitHub(username = "@bnorm", modifier = Modifier.weight(1f).fillMaxHeight())
        }
    }
}
