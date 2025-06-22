package dev.bnorm.dcnyc25.samples

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.storyboard.easel.animateEnterExit
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement

class Piece(
  val text: AnnotatedString,
  val key: String? = null,
  val crossFade: Boolean = false,
) {
  fun isLineEnd(): Boolean = false
}

fun computePieces(
  before: AnnotatedString,
  after: AnnotatedString,
): Pair<List<Piece>, List<Piece>> {
  TODO()
}

@Composable
fun MagicText(
  before: AnnotatedString,
  after: AnnotatedString,
  asAfter: Boolean,
) {
  val (bPieces, aPieces) =
    remember(before, after) {
      computePieces(before, after)
    }

  SharedTransitionLayout {
    AnimatedContent(asAfter) { asAfter ->
      val pieces: List<Piece> =
        if (asAfter) aPieces else bPieces

      Column {
        val iter = pieces.iterator()
        while (iter.hasNext()) {
          Row {
            while (iter.hasNext()) {
              val piece = iter.next()
              if (piece.isLineEnd()) break
              Piece(piece)
            }
          }
        }
      }
    }
  }
}

@Composable
context(
  _: AnimatedVisibilityScope,
  _: SharedTransitionScope
)
fun RowScope.Piece(piece: Piece) {
  val modifier = when {
    piece.key == null ->
      Modifier.animateEnterExit()
    piece.crossFade -> Modifier.sharedBounds(
      rememberSharedContentState(piece.key),
      // ...
    )
    else -> Modifier.sharedElement(
      rememberSharedContentState(piece.key),
      // ...
    )
  }

  Text(piece.text, modifier.alignByBaseline())
}
