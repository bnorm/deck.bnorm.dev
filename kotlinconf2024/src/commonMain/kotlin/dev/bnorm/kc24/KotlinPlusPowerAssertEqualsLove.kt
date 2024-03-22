package dev.bnorm.kc24

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.SocialGitHub
import dev.bnorm.kc24.elements.SocialMastodon
import dev.bnorm.kc24.sections.*
import dev.bnorm.kc24.template.KodeeLoving
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.SectionHeader
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.animation.animateList
import dev.bnorm.librettist.animation.startAnimation
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.section
import dev.bnorm.librettist.text.thenLineEndDiff

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove(
    state: AssertionLibrariesState = AssertionLibrariesState(),
) {
    slide { Title() }

    // TODO
    //  - make sure the main point here is emphasized!!!
    //  - improve the flow between the section

    // TODO instead of going through libraries, do we instead end with a power-assert example?
    //  - gets us to the point a little faster
    //  - good assertions -> libraries -> power-assert -> all setup -> more examples, is sort of a weird flow anyways
    //  - good assertions -> initial example -> basic setup -> more examples -> advanced setup, may be a better flow
    //  - comparing this with groovy may lead to a need to talk a little about the history from the groovy perspective (doubt people will care)
    //  - shortens the talk a little so we can talk about other things (more in-depth custom function requirements?)
    //  - Gradle setup seems like a good next transition and we can dive right in
    // TODO use a pros/cons list instead of a red squiggly lines?
    //  - can draw attention with green/red icons for each point
    section(title = "Good Assertions") {
        SectionHeader(animateToBody = true)
        GoodAssertions()
        SectionHeader(animateFromBody = true)
    }

    SectionChange(previousTitle = "Good Assertions", nextTitle = "Assertion Libraries")

    // TODO the verbal transition between these sections is not great...
    //  - go from the last example of a good assertion...
    //  - ...to talking about the number of assertion libraries
    //  - huh?!

    // TODO should we talk about Java test libraries to really drive home the point?
    //  - wouldn't all the java libraries also be groovy libraries?
    //  - MAKE SURE THAT WE ARE NOT BASHING THESE OTHER LIBRARIES!!!
    section(title = "Assertion Libraries") {
        SectionHeader(animateToBody = true)
        AssertionLibraries(state)
        SectionHeader(animateFromBody = true)
    }

    SectionHeader(title = "I would assert...")
    SectionChange(previousTitle = "I would assert...", nextTitle = "Power-Assert")

    // TODO this intro feels a little weak and slow?
    //  - jump right into the example with power-assert?
    section(title = "Power-Assert") {
        SectionHeader(animateToBody = true)
        PowerAssertIntro()
        SectionHeader(animateFromBody = true)
    }

    SectionChange(previousTitle = "Power-Assert", nextTitle = "Power-Assert Setup")

    // TODO feels a little slow to get started
    //  - too many clicks to get to the point? just be faster with the clicks?
    //  - are we still talking about Groovy?
    //  - combine with intro?
    //  - configuration feels a little out of place? tell them to look up the documentation?
    //  - Amper for adding compiler plugin?
    section(title = "Power-Assert Setup") {
        SectionHeader(animateToBody = true)
        PowerAssertSetup()
        SectionHeader(animateFromBody = true)
    }

    SectionChange(previousTitle = "Power-Assert Setup", nextTitle = "Advanced Power-Assert")

    // TODO there maybe needs to be a little more separation of the examples
    //  - going from example power-assert, to setup, to advanced examples is an awkward transition
    //  - maybe start with setup?
    //  - side-panel setup? then all Gradle stuff is side-paneled which is nicely consistent
    //  - and then continuing with the examples feels a little more natural
    //  - have an example which shows function configuration; do we need an example for source-set exclusion?
    section(title = "Advanced Power-Assert") {
        SectionHeader(animateToBody = true)
        AdvancedPowerAssert()
        SectionHeader(animateFromBody = true)
    }

    SectionChange(previousTitle = "Advanced Power-Assert", nextTitle = "Future of Power-Assert")

    // TODO should there be an internals section?
    //  - explain more how functions are transformed and signature requirements

    section(title = "Future of Power-Assert") {
        SectionHeader(animateToBody = true)
        PowerAssertFuture()
        SectionHeader(animateFromBody = true)
    }

    // TODO summary slide?
    //  - make this the last slide instead of the ThankYou
    //  - kotl.in shorted links
    //    - link to docs for plugin? kotl.in/power-assert?
    //    - add link to kotlin-lang Slack channel? kotl.in/power-assert-slack?
    //    - add link to the slide show itself? deck.bnorm.dev/kotlinconf2024
    //    - make all of these clickable
    //  - QR code?

    slide { ThankYou() }
}

private fun ShowBuilder.SectionChange(previousTitle: String, nextTitle: String) {
    slide(advancements = 0) {
        SectionHeader(showAsBody = updateTransition(false)) {
            val values = startAnimation(previousTitle).thenLineEndDiff(nextTitle).sequence.toList()
            val text by transition.animateList(values) { if (it == 0) values.lastIndex else 0 }
            Text(text)
        }
    }
}

@Composable
fun Title() {
    TitleSlide {
        Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Center)) {
                Text("Kotlin + Power-Assert = ")
                KodeeLoving(modifier = Modifier.requiredSize(200.dp).graphicsLayer { rotationY = 180f })
            }

            PresenterSocials(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun ThankYou() {
    TitleSlide {
        Box(modifier = Modifier.fillMaxSize().padding(SLIDE_PADDING)) {
            Text("Thank You!", modifier = Modifier.align(Alignment.Center))

            PresenterSocials(Modifier.align(Alignment.BottomCenter))
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
