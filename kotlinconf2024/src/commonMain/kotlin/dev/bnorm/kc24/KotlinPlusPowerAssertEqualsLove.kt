package dev.bnorm.kc24

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
import dev.bnorm.kc24.sections.*
import dev.bnorm.kc24.template.KodeeLoving
import dev.bnorm.kc24.template.SLIDE_PADDING
import dev.bnorm.kc24.template.TitleSlide
import dev.bnorm.librettist.show.ShowBuilder

fun ShowBuilder.KotlinPlusPowerAssertEqualsLove(
    state: AssertionLibrariesState = AssertionLibrariesState(),
) {
    slide { Title() }
    GoodAssertions()
    AssertionLibraries(state)
    PowerAssertIntro()
    PowerAssertSetup()
    AdvancedPowerAssert()
    PowerAssertFuture()
    slide { ThankYou() }
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
