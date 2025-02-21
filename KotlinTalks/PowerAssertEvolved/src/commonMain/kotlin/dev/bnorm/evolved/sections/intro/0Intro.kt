package dev.bnorm.evolved.sections.intro

import dev.bnorm.evolved.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.Intro() {
    SectionAndTitle("What's Power-Assert?") {
        Overview()
        Timeline()
    }
}

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "
