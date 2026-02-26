package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.StageParse() {
    val items = listOf(
        AnnotatedString("$BULLET_1 When Kotlin code is parsed it is converted into FIR."),
        buildAnnotatedString {
            append("$BULLET_1 FIR is a tree-based representation of the ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                append("structure")
            }
            append(" of Kotlin code.")
        },
    )

    StageDetail(frameCount = items.size + 1 + 14, stage = CompilerStage.Parse) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(transition.createChildTransition { it.toValue() - 1 }) {
                for (value in items) {
                    item { Text(value) }
                }
            }
            if (transition.currentState.toValue() > items.size) {
                transition.createChildTransition { it.toValue() - items.size - 1 }.FirTree()
            }
        }
    }
}
