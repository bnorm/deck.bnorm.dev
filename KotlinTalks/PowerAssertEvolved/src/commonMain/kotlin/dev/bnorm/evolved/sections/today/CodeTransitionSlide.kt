package dev.bnorm.evolved.sections.today

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import dev.bnorm.evolved.template.HeaderAndBody
import dev.bnorm.evolved.template.code.MagicCode
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.CodeTransitionSlide(transitions: List<Pair<String, String>>) {
    scene(stateCount = transitions.size + 1) {
        HeaderAndBody {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).height(IntrinsicSize.Max)) {
                frame.createChildTransition { it.toState() }
                    .MagicCode(transitions)
            }
        }
    }
}
