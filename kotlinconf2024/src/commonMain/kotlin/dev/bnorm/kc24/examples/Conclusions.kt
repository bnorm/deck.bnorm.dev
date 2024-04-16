package dev.bnorm.kc24.examples

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.kc24.elements.defaultSpec
import dev.bnorm.kc24.template.SLIDE_CONTENT_SPACING
import kotlinx.collections.immutable.ImmutableList

sealed interface Conclusion {
    val text: String

    data class Pro(override val text: String) : Conclusion
    data class Con(override val text: String) : Conclusion
}

@Composable
fun Transition<out Int>.ShowConclusions(
    conclusions: ImmutableList<Conclusion>,
) {
    val withIndex = remember(conclusions) { conclusions.withIndex() }

    @Composable
    fun Conclusion(index: Int, text: String, pro: Boolean) {
        AnimatedVisibility(
            visible = { it > index },
            enter = fadeIn(defaultSpec()),
            exit = fadeOut(defaultSpec()),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (pro) {
                    Icon(
                        Icons.Filled.Done, tint = Color.White, contentDescription = "",
                        modifier = Modifier.size(48.dp).background(Color(0xFF009900), shape = CircleShape)
                    )
                } else {
                    Icon(
                        Icons.Filled.Close, tint = Color.White, contentDescription = "",
                        modifier = Modifier.size(48.dp).background(Color(0xFF990000), shape = CircleShape)
                    )
                }
                Spacer(Modifier.width(16.dp))
                Text(text)
            }
        }
    }

    Row(modifier = Modifier.fillMaxWidth().padding(SLIDE_CONTENT_SPACING)) {
        Column(modifier = Modifier.weight(1f)) {
            for ((index, conclusion) in withIndex.filter { it.value is Conclusion.Pro }) {
                Conclusion(index, conclusion.text, pro = true)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            for ((index, conclusion) in withIndex.filter { it.value is Conclusion.Con }) {
                Conclusion(index, conclusion.text, pro = false)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
