package dev.bnorm.kc26.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.badge
import dev.bnorm.deck.story.generated.resources.phone
import dev.bnorm.kc26.template.Kc26Colors
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Closing() {
    scene(
        enterTransition = { fadeIn(animationSpec = tween(750, delayMillis = 750)) },
        exitTransition = { fadeOut(animationSpec = tween(750)) },
    ) {
        Surface(
            border = BorderStroke(2.dp, Kc26Colors.colorGradient),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val visible = transition.createChildTransition { it is Frame.Value }
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
                    enter = slideInVertically(tween(800, easing = EaseOutBounce)) { -it },
                    exit = slideOutVertically(tween(500, delayMillis = 500, easing = EaseInCubic)) { -it },
                ) {
                    ResourceImage(
                        Res.drawable.badge,
                        modifier = Modifier
                            .size(218.7f.dp, 401.4f.dp)
                            .offset(472.dp, 0.dp)
                    )
                }

                visible.AnimatedVisibility(
                    visible = { it },
                    enter = slideInHorizontally(tween(500, easing = EaseOutCubic)) { 5 * it / 4 },
                    exit = slideOutHorizontally(tween(500, delayMillis = 500, easing = EaseInCubic)) { 5 * it / 4 },
                ) {
                    // TODO there's a subtle border around the phone in the template
                    //  due to another background image behind the phone from 2025
                    ResourceImage(
                        Res.drawable.phone,
                        modifier = Modifier
                            .size(217.4f.dp, 401.4f.dp)
                            .offset(716f.dp, 62.7f.dp)
                    )
                }

                DrawArc(arcFraction)

                Column(Modifier.align(Alignment.TopStart).padding(start = 40.dp, top = 40.dp)) {
                    val handleStyle = MaterialTheme.typography.body2 + TextStyle(fontFamily = JetBrainsMono)
                    Mastodon(username = "bnorm@kotlin.social", style = handleStyle)
                    Spacer(Modifier.size(4.dp))
                    Bluesky(username = "@bnorm.dev", style = handleStyle)
                }
                Column(Modifier.align(Alignment.BottomStart).padding(start = 40.dp, bottom = 28.dp)) {
                    ProvideTextStyle(
                        MaterialTheme.typography.h2.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 54.sp,
                            lineHeight = 54.sp,
                        )
                    ) {
                        Text("Thank You,\nand Don't\nForget to Vote")
                    }
                }
            }
        }
    }
}


@Composable
private fun DrawArc(fraction: Float) {
    Canvas(Modifier.fillMaxSize()) {
        val xEnd = 1488.0f
        val yEnd = 962.0f
        val topLeft = Offset(1168.0f, 620.0f)
        val size = Size(388.0f, 388.0f)

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
            drawArc(brush, 180f, angle, false, topLeft, size, style = Stroke(5.0f))

            if (fraction > 1f) {
                val arrowFraction = (fraction - 1f).coerceAtLeast(0f) * 5f
                val left = lerp(Offset(xEnd, yEnd), Offset(xEnd - 27, yEnd - 3), arrowFraction)
                val right = lerp(Offset(xEnd, yEnd), Offset(xEnd - 3, yEnd + 27), arrowFraction)
                drawLine(brush, Offset(xEnd + 1, yEnd), left, strokeWidth = 5.0f)
                drawLine(brush, Offset(xEnd, yEnd - 1), right, strokeWidth = 5.0f)
            }
        }
    }
}