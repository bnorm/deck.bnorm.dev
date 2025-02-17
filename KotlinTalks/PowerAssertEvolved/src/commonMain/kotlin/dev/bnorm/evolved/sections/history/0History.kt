package dev.bnorm.evolved.sections.history

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.History() {
    SectionAndTitle("How Did It Get To Kotlin?") {
        Timeline()
        // TODO anything else from the history we want to detail?
    }
}
