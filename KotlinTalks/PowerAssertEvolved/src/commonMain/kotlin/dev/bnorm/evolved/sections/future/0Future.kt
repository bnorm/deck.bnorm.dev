package dev.bnorm.evolved.sections.future

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Future() {
    SectionAndTitle("What's next?") {
        SyntaxIdea()
        // TODO other expression types like string template for println
        // TODO local variables
        // TODO IntelliJ
    }
}
