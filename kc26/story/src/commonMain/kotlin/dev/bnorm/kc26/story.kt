package dev.bnorm.kc26

import dev.bnorm.kc26.sections.TimelineScene
import dev.bnorm.kc26.sections.TimelineState
import dev.bnorm.kc26.sections.Closing
import dev.bnorm.kc26.sections.FutureSection
import dev.bnorm.kc26.sections.MajorSection
import dev.bnorm.kc26.sections.MinorSection
import dev.bnorm.kc26.sections.StartSection
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
    // TODO go through samples and pick a fun theme
    //  - LOTR again?
    //  - star trek?
    //  - make it a choice from the audience companion?!
    //  - examples should include...
    //      - an array to show improvement there
    //      - a String to show quotes

    // TODO Companion
    //  - survey of who knows/uses power-assert
    //  - scene reactions?

    // TODO Pre-slide: survey of who knows/uses power-assert
    //  - use companion to survey audience about their experience with power-assert

    Title()

    // TODO pick 1: all examples include '@Test' or all examples don't.
    TimelineScene(end = TimelineState.Bundled)
    StartSection()
    TimelineScene(start = TimelineState.Bundled, end = TimelineState.Improvements)
    MinorSection()
    TimelineScene(start = TimelineState.Improvements, end = TimelineState.Explanations)
    MajorSection()
    TimelineScene(start = TimelineState.Explanations, end = TimelineState.Future)
    FutureSection()

    Closing()
}
