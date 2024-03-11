package dev.bnorm.kc24.elements

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SocialMastodon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(DrawableResource("social/mastodon.png")),
        contentDescription = "",
        modifier = modifier,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SocialGitHub(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(DrawableResource("social/github-white.png")),
        contentDescription = "",
        modifier = modifier,
    )
}
