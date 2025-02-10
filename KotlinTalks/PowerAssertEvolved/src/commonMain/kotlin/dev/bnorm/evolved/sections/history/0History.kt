package dev.bnorm.evolved.sections.history

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.History() {
    SectionAndTitle("A Brief History") {
        Timeline()
    }
}
