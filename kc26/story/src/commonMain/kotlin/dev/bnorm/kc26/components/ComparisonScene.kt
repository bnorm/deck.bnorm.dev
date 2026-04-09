package dev.bnorm.kc26.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import dev.bnorm.kc26.template.DarkBox
import dev.bnorm.kc26.template.code1
import dev.bnorm.kc26.template.gradientBackground

@Composable
fun SampleComparison(
    sample: @Composable () -> Unit,
    beforeVersion: @Composable () -> Unit,
    beforeOutput: @Composable () -> Unit,
    afterVersion: @Composable () -> Unit,
    afterOutput: @Composable () -> Unit,
    hideOutput: Transition<Boolean>,
    showAfter: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        ProvideTextStyle(MaterialTheme.typography.code1) {
            Box(Modifier.padding(32.dp)) {
                sample()
            }
        }

        val fraction by hideOutput.animateFloat(
            transitionSpec = { tween(300, easing = EaseInOut) },
        ) { if (it) 1f else 0f }

        // TODO make this a constant size
        //  so it rises from the bottom without animating title placement.
        //  also so the 92 padding is not needed between sample and output
//        Spacer(Modifier.size(32.dp))
        Spacer(Modifier.fillMaxHeight(fraction))
        Spacer(Modifier.height(2.dp).fillMaxWidth().gradientBackground())
        OutputComparison(
            beforeVersion = beforeVersion,
            beforeOutput = beforeOutput,
            afterVersion = afterVersion,
            afterOutput = afterOutput,
            showAfter = showAfter,
        )
    }
}

@Composable
fun OutputComparison(
    beforeVersion: @Composable () -> Unit,
    beforeOutput: @Composable () -> Unit,
    afterVersion: @Composable () -> Unit,
    afterOutput: @Composable () -> Unit,
    showAfter: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color.Black,
        contentColor = Color.White,
        modifier = modifier,
    ) {
        DarkBox {
            VersionWipe(
                showAfter = showAfter,
                beforeTitle = beforeVersion,
                beforeOutput = beforeOutput,
                afterTitle = afterVersion,
                afterOutput = afterOutput,
            )
        }
    }
}

enum class VersionCompareState {
    Hidden,
    Before,
    After,
    ;
}

@Composable
private fun VersionWipe(
    showAfter: Transition<Boolean>,
    beforeTitle: @Composable () -> Unit,
    beforeOutput: @Composable () -> Unit,
    afterTitle: @Composable () -> Unit,
    afterOutput: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    fun spec(): TweenSpec<Float> = tween(2000, easing = EaseInOut)

    // TODO what's the best way to align the output?
    //  - seems correct to left-justify it
    //  - but that looks weird in the "after" state with big gaps
    //  - should this be a box?
    //    - versions in the bottom corners?
    Row(modifier) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.width(128.dp).fillMaxHeight()) {
            showAfter.AnimatedVisibility(
                visible = { !it },
                enter = fadeIn(spec()), exit = fadeOut(spec()),
            ) {
                ProvideTextStyle(MaterialTheme.typography.h2) {
                    beforeTitle()
                }
            }
        }
        Spacer(Modifier.width(2.dp).fillMaxHeight().gradientBackground())
        Box(Modifier.weight(1f).fillMaxHeight()) {
            // TODO this animation causes all animations after it to take just as long
            //  is there some way we can detach it when complete?
            val clipPercent by showAfter.animateFloat(
                transitionSpec = { spec() }
            ) {
                if (it) 1f else 0f
            }

            Box(Modifier.drawWithContent {
                clipRect(left = clipPercent * size.width) {
                    this@drawWithContent.drawContent()
                }
            }) {
                Box(Modifier.fillMaxSize().padding(16.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        beforeOutput()
                    }
                }
            }
            Box(Modifier.drawWithContent {
                clipRect(right = clipPercent * size.width) {
                    this@drawWithContent.drawContent()
                }
            }) {
                Box(Modifier.fillMaxSize().padding(16.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        afterOutput()
                    }
                }
            }
            BoxWithConstraints(Modifier.matchParentSize()) {
                Spacer(
                    Modifier
                        .width(2.dp).offset(x = (maxWidth + 2.dp) * clipPercent - 2.dp)
                        .fillMaxHeight().gradientBackground()
                )
            }
        }
        Spacer(Modifier.width(2.dp).fillMaxHeight().gradientBackground())
        Box(contentAlignment = Alignment.Center, modifier = Modifier.width(128.dp).fillMaxHeight()) {
            showAfter.AnimatedVisibility(
                visible = { it },
                enter = fadeIn(spec()), exit = fadeOut(spec()),
            ) {
                ProvideTextStyle(MaterialTheme.typography.h2) {
                    afterTitle()
                }
            }
        }
    }
}
