package dev.bnorm.dcnyc25.samples

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString

fun buildSequence(
  before: AnnotatedString,
  after: AnnotatedString,
): List<AnnotatedString> {
  TODO()
}

@Composable
fun MagicLineEnding(
  before: AnnotatedString,
  after: AnnotatedString,
  asAfter: Boolean,
) {
  val sequence: List<AnnotatedString> =
    remember(before, after) {
      buildSequence(before, after)
    }

  val index: Int by animateIntAsState(
    targetValue = when {
      asAfter -> sequence.lastIndex
      else -> 0
    },
    animationSpec = tween(
      durationMillis = 35 * sequence.size,
      easing = LinearEasing
    )
  )

  Text(sequence[index])
}
