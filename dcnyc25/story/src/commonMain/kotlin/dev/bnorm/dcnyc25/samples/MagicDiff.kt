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
import dev.bnorm.storyboard.easel.sharedElement

fun computeEditScript(
  before: AnnotatedString,
  after: AnnotatedString,
): List<Edit> {
  TODO()
}

sealed class Edit(
  val key: Any,
  val text: AnnotatedString,
) {
  class Match(key: Any, text: AnnotatedString) :
    Edit(key, text)

  class Add(key: Any, text: AnnotatedString) :
    Edit(key, text)

  class Delete(key: Any, text: AnnotatedString) :
    Edit(key, text)
}

@Composable
fun MagicDiff(
  before: AnnotatedString,
  after: AnnotatedString,
  asAfter: Boolean,
) {
  val editScript: List<Edit> =
    remember(before, after) {
      computeEditScript(before, after)
    }

  SharedTransitionLayout {
    AnimatedContent(asAfter) { asAfter ->
      Column {
        val iter = editScript.iterator()
        while (iter.hasNext()) {
          Row {
            while (iter.hasNext()) {
              val edit = iter.next()
              Edit(edit, asAfter)
              // ...
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
fun RowScope.Edit(edit: Edit, asAfter: Boolean) {
  val modifier = when (edit) {
    is Edit.Match -> Modifier.sharedElement(
      rememberSharedContentState(edit.key),
    )

    else -> Modifier.animateEnterExit()
  }

  if (
    edit is Edit.Match ||
    (edit is Edit.Add && asAfter) ||
    (edit is Edit.Delete && !asAfter)
  ) {
    Text(edit.text, modifier.alignByBaseline())
  }
}
