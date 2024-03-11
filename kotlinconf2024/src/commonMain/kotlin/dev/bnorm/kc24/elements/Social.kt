package dev.bnorm.kc24.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SocialMastodon(username: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(DrawableResource("social/mastodon.png")),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(username)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SocialGitHub(username: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(DrawableResource("social/github-white.png")),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(username)
    }
}
