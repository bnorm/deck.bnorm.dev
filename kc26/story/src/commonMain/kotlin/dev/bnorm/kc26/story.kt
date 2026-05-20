package dev.bnorm.kc26

import dev.bnorm.kc26.sections.TimelineScene
import dev.bnorm.kc26.sections.TimelineState
import dev.bnorm.kc26.sections.Closing
import dev.bnorm.kc26.sections.FutureSection
import dev.bnorm.kc26.sections.ExplanationsSection
import dev.bnorm.kc26.sections.ImprovementsSection
import dev.bnorm.kc26.sections.BundledSection
import dev.bnorm.kc26.sections.Title
import dev.bnorm.kc26.template.gradientDecorator
import dev.bnorm.kc26.template.themeDecorator
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(
    decorator: ContentDecorator = ContentDecorator.None,
): Storyboard = Storyboard.build(
    title = "Powering Up Your Assertions",
    description = """
        Big changes to Power-Assert are headed your way with Kotlin 2.4! We’ve heard your complaints about complex
        Gradle configuration, confusing message diagrams, and poor library integration. We’re hoping to address all of
        these areas, and I’ll show you how!

        But the good news doesn’t stop there. If you’re an assertion library author, you may be interested in how you
        can add first-party support for Power-Assert to your library. We want every assertion library to have the same
        great, out-of-the-box experience!

        And we might even take a brief look at what we’ll be working on next!
    """.trimIndent(),
    decorator = ContentDecorator.from(
        themeDecorator(),
        decorator,
        gradientDecorator(),
    ),
) {
    Title()

    TimelineScene(end = TimelineState.Bundled)
    BundledSection()
    TimelineScene(start = TimelineState.Bundled, end = TimelineState.Improvements)
    ImprovementsSection()
    TimelineScene(start = TimelineState.Improvements, end = TimelineState.Explanations)
    ExplanationsSection()
    TimelineScene(start = TimelineState.Explanations, end = TimelineState.Future)
    FutureSection()

    // TODO need a smoother transition to closing?
    Closing()
}
