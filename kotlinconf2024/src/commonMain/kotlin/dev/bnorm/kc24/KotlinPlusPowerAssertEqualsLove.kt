package dev.bnorm.kc24

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.SocialGitHub
import dev.bnorm.kc24.elements.SocialMastodon
import dev.bnorm.kc24.sections.Future
import dev.bnorm.kc24.sections.GoodAssertions
import dev.bnorm.kc24.sections.PowerAssertExamples
import dev.bnorm.kc24.template.*
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.section
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove() {
    slide { Title() }

    // TODO
    //  - make sure the main point here is emphasized!!!
    //  - improve the flow between the section

    // TODO slide transition between sections?

    section(title = "Crafting an Assertion") {
        SectionHeader(animateToBody = true)
        GoodAssertions()
    }

    section(title = "Why Power-Assert?") {
        SectionHeader(animateToBody = true)
        PowerAssertExamples()
    }

    section(title = "A Look at the Future") {
        SectionHeader(animateToBody = true)
        Future()
    }

    slide { Summary() }
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
