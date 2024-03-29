package dev.bnorm.kc24

import androidx.compose.animation.core.updateTransition
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
import dev.bnorm.kc24.sections.Examples
import dev.bnorm.kc24.sections.Assertions
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

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove() {
    slide { Title() }

    // TODO
    //  - make sure the main point here is emphasized!!!
    //  - improve the flow between the section

    section(title = "Assertions") {
        SectionHeader(animateToBody = true)
        Assertions()
    }

    section(title = "Examples") {
        SectionHeader(animateToBody = true)
        Examples()
    }

    // TODO should there be an advanced section?
    //  - creating our own functions (assertSoftly?) -> function doesn't need to throw
    //    - explain more how functions are transformed and signature requirements

    section(title = "Future") {
        SectionHeader(animateToBody = true)
        Future()
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
