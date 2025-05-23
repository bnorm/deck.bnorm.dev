package dev.bnorm.kc25.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.end_badge
import dev.bnorm.deck.story.generated.resources.end_phone
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.enter
import dev.bnorm.storyboard.easel.template.exit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Closing() {
    scene(
        stateCount = 1,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.CenterEnd),
            end = SceneEnter(alignment = Alignment.CenterStart),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.CenterEnd),
            end = SceneExit(alignment = Alignment.CenterStart),
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val visible = transition.createChildTransition { it is Frame.State }
            val arcFraction by visible.animateFloat(
                transitionSpec = {
                    when (targetState) {
                        true -> tween(500, delayMillis = 500, easing = EaseInCubic)
                        false -> tween(500, easing = EaseOutCubic)
                    }
                }
            ) {
                when (it) {
                    true -> 1.2f
                    false -> 0f
                }
            }

            visible.AnimatedVisibility(
                visible = { it },
                enter = slideInVertically(tween(1200, easing = EaseOutElastic)) { -it },
                exit = slideOutVertically(tween(500, delayMillis = 500, easing = EaseInCubic)) { -it },
            ) {
                Image(
                    painter = painterResource(Res.drawable.end_badge),
                    contentDescription = "",
                    modifier = Modifier.size(225.3.dp, 400.6.dp).offset(479.3.dp, 0.dp),
                )
            }

            visible.AnimatedVisibility(
                visible = { it },
                enter = slideInHorizontally(tween(500, easing = EaseOutCubic)) { 5 * it / 4 },
                exit = slideOutHorizontally(tween(500, delayMillis = 500, easing = EaseInCubic)) { 5 * it / 4 },
            ) {
                Image(
                    painter = painterResource(Res.drawable.end_phone),
                    contentDescription = "",
                    modifier = Modifier.size(240.dp, 470.6.dp).offset(703.3.dp, 0.dp),
                )
            }

            DrawArc(arcFraction)

            Column(Modifier.align(Alignment.TopStart).padding(32.dp)) {
                Mastodon(username = "bnorm@kotlin.social")
                Spacer(Modifier.size(4.dp))
                Bluesky(username = "@bnorm.dev")
            }
            Column(Modifier.align(Alignment.BottomStart).padding(32.dp)) {
                ProvideTextStyle(MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)) {
                    Text("Thank You,")
                    Text("and Don't")
                    Text("Forget to Vote")
                }
            }
        }
    }
}

@Composable
private fun DrawArc(fraction: Float) {
    Canvas(Modifier.fillMaxSize()) {
        val xEnd = 744.dp.toPx()
        val yEnd = 481.dp.toPx()
        val topLeft = Offset(584.dp.toPx(), 310.dp.toPx())
        val size = Size(194.dp.toPx(), 194.dp.toPx())

        val brush = Brush.horizontalGradient(
            startX = topLeft.x,
            endX = xEnd,
            colors = listOf(
                Color(0xFFE901FF),
                Color(0xFFFD061C),
            )
        )

        if (fraction > 0f) {
            val angleFraction = fraction.coerceAtMost(1f)
            val angle = lerp(0f, -130f, angleFraction)
            drawArc(brush, 180f, angle, false, topLeft, size, style = Stroke(4.0f))

            if (fraction > 1f) {
                val arrowFraction = (fraction - 1f).coerceAtLeast(0f) * 5f
                val left = lerp(Offset(xEnd, yEnd), Offset(xEnd - 27, yEnd - 3), arrowFraction)
                val right = lerp(Offset(xEnd, yEnd), Offset(xEnd - 3, yEnd + 27), arrowFraction)
                drawLine(brush, Offset(xEnd + 1, yEnd), left, strokeWidth = 4.0f)
                drawLine(brush, Offset(xEnd, yEnd - 1), right, strokeWidth = 4.0f)
            }
        }
    }
}