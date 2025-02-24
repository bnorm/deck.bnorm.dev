package dev.bnorm.kc25

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.bnorm.deck.kotlinconf2025.generated.resources.*
import dev.bnorm.deck.shared.socials.Bluesky
import dev.bnorm.deck.shared.socials.JetBrainsEmployee
import dev.bnorm.deck.shared.socials.Mastodon
import dev.bnorm.kc25.template.THEME_DECORATOR
import dev.bnorm.kc25.template.defaultSpec
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.core.toInt
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.milliseconds

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Writing Your Third Kotlin Compiler Plugin",
        description = """
            Compiler plugins have become an integral part of a developer’s experience with the Kotlin programming
            language. Areas like UI development (Jetpack Compose), backend development (Spring Boot / Kotlin
            Serialization), and even testing (Power-Assert) all leverage compiler plugins. But what is a Kotlin compiler
            plugin and what can it do? Let’s learn by writing one!

            In this talk, we’ll discuss use cases for compiler plugins and learn how they integrate with the Kotlin
            compiler. Then we’ll explore everything related to how code is represented within the Kotlin compiler,
            including how to inspect, navigate, transform, and create these representations. When we’re done, we’ll have
            written a compiler plugin from scratch which can navigate the project code, inspect annotations, generate
            boilerplate, and even report errors!
        """.trimIndent(),
        decorator = THEME_DECORATOR,
    ) {
        Title()
        Closing()
    }
}

private fun StoryboardBuilder.Title() {
    slide(
        stateCount = 1,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(Res.drawable.start_background),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                )
                Image(
                    painter = painterResource(Res.drawable.start_conference),
                    contentDescription = "",
                    modifier = Modifier.size(247.dp, 26.dp).offset(40.dp, 40.dp),
                )
            }

            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Column(
                    Modifier.fillMaxWidth().align(Alignment.BottomStart),
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(Modifier.padding(16.dp)) {
                        ProvideTextStyle(MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)) {
                            Text("Writing Your Third")
                            Text("Kotlin Compiler Plugin")
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.align(Alignment.CenterStart)
                                .padding(16.dp),
                        ) {
                            JetBrainsEmployee(
                                name = "Brian Norman",
                                title = "Kotlin Compiler Developer",
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.align(Alignment.CenterEnd)
                                .background(
                                    // TODO use haze instead?
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Black.copy(alpha = 0f),
                                            Color.Black.copy(alpha = 0.7f),
                                            Color.Black.copy(alpha = 0.7f),
                                            Color.Black.copy(alpha = 0f),
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .padding(16.dp),
                        ) {
                            Bluesky(username = "@bnorm.dev")
                            Spacer(Modifier.size(4.dp))
                            Mastodon(username = "bnorm@kotlin.social")
                        }
                    }
                }
            }
        }
    }
}

private fun StoryboardBuilder.Closing() {
    slide(
        stateCount = 1,
        enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
        exitTransition = SlideExit(alignment = Alignment.CenterEnd),
    ) {
        val slideInAnimationSpec = defaultSpec<IntOffset>()
        val slideOutAnimationSpec = defaultSpec<IntOffset>(delay = 300.milliseconds)
        val arcInAnimationSpec = defaultSpec<Float>(delay = 300.milliseconds)
        val arcOutAnimationSpec = defaultSpec<Float>()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val arcFraction by state.animateFloat(
                transitionSpec = { if (targetState.toInt() >= 0) arcInAnimationSpec else arcOutAnimationSpec }
            ) {
                if (it.toInt() >= 0) 1.2f else 0f
            }

            state.AnimatedVisibility(
                visible = { it.toInt() >= 0 },
                enter = slideInVertically(initialOffsetY = { -it }, animationSpec = slideInAnimationSpec),
                exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = slideOutAnimationSpec),

                ) {
                Image(
                    painter = painterResource(Res.drawable.end_badge),
                    contentDescription = "",
                    modifier = Modifier.size(225.3.dp, 400.6.dp).offset(479.3.dp, 0.dp),
                )
            }

            state.AnimatedVisibility(
                visible = { it.toInt() >= 0 },
                enter = slideInHorizontally(initialOffsetX = { 5 * it / 4 }, animationSpec = slideInAnimationSpec),
                exit = slideOutHorizontally(targetOffsetX = { 5 * it / 4 }, animationSpec = slideOutAnimationSpec),
            ) {
                Image(
                    painter = painterResource(Res.drawable.end_phone),
                    contentDescription = "",
                    modifier = Modifier.size(240.dp, 470.6.dp).offset(703.3.dp, 0.dp),
                )
            }

            DrawArc(arcFraction)

            Column(Modifier.align(Alignment.TopStart).padding(32.dp)) {
                Bluesky(username = "@bnorm.dev")
                Spacer(Modifier.size(4.dp))
                Mastodon(username = "bnorm@kotlin.social")
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
