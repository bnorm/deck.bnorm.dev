package dev.bnorm.evolved

import dev.bnorm.evolved.sections.evolution.Evolution
import dev.bnorm.evolved.sections.future.Future
import dev.bnorm.evolved.sections.history.History
import dev.bnorm.evolved.sections.today.Today
import dev.bnorm.evolved.template.THEME_DECORATOR
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.plus
import org.jetbrains.compose.reload.DevelopmentEntryPoint

private val DEV = SlideDecorator { content ->
    DevelopmentEntryPoint {
        content()
    }
}

fun createStoryboard() = Storyboard.build(
    title = "Power-Assert: Evolved",
    description = """
        What?
        Kotlin Power-Assert is evolving!
        
        5 years ago, a simple question on the KotlinLang Slack launched a journey to bring power-assert to the Kotlin
        language. But now that power-assert is a native compiler-plugin for Kotlin, what does the next evolution look
        like? Join me to hear a brief history of where power-assert came from, how it works today, and what it may look
        like in the future!
    """.trimIndent(),
    size = Storyboard.DEFAULT_SIZE,
    decorator = DEV + THEME_DECORATOR,
) {
    // TODO title slide?
    History()
    Today()
    Evolution()
    Future()
    // TODO closing slide?
}
