package dev.bnorm.evolved.sections.future

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Future() {
    SectionAndTitle("And After That?") {
        Debug()
        LocalVariables()
        IntelliJ()
        Junie()
        SyntaxIdea()
    }
}
