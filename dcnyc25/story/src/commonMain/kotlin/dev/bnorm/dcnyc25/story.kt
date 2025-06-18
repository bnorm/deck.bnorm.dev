package dev.bnorm.dcnyc25

import dev.bnorm.dcnyc25.sections.*
import dev.bnorm.dcnyc25.template.FirstSample
import dev.bnorm.dcnyc25.template.SecondSample
import dev.bnorm.dcnyc25.template.ThirdSample
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard

/**
 * ### General Ideas
 * * lots of background color blocking with the primary and secondary colors
 *
 * ### Outline
 * * code change
 * * (?)background
 * * line end diffing
 * * myers diffing
 * * myers + patience diffing
 * * myers + patience tuning
 * * how it's still broken
 * * DiffUtil
 * * TODO how can we bring this back into Compose: SharedTransitionLayout?
 * * (?)Storyboard
 *
 * TODO show how different string diff algorithms work
 *  * https://www.nathaniel.ai/myers-diff/
 *  * https://blog.jcoglan.com/2017/02/12/the-myers-diff-algorithm-part-1/
 *  * https://blog.jcoglan.com/2017/02/15/the-myers-diff-algorithm-part-2/
 *  * https://blog.jcoglan.com/2017/02/17/the-myers-diff-algorithm-part-3/
 *  * https://blog.jcoglan.com/2017/03/22/myers-diff-in-linear-space-theory/
 *  * https://blog.jcoglan.com/2017/04/25/myers-diff-in-linear-space-implementation/
 *  * https://blog.jcoglan.com/2017/09/19/the-patience-diff-algorithm/
 * TODO this is all just DiffUtil?!
 *  * https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil
 */
fun createStoryboard(
    decorator: SceneDecorator = storyDecorator(),
): Storyboard = Storyboard.build(
    title = "(Re)creating Magic(Move) with Compose",
    description = """
        Presentation software is extremely powerful, full of creative styling, powerful animations, and plenty of
        other things. Yet every time I wrote a new presentation, I found myself repeating something with almost
        every slide; whether that be the content, styling, or animations. Wanting the torment to end (and a
        different kind of torment to begin), I wrote my own presentation framework with Compose!

        But wanting to be like all the cool presenters, I needed something like Keynote’s Magic Move for all my code
        examples! Compose has SharedTransitionLayout, how hard could this be? So I built my own version of Magic
        Move. And rebuilt it. And rebuilt it again. And Again. (And probably again between submitting and actually
        giving this talk) Let’s look at all those different iterations, the improvements each brought, and why
        diffing algorithms are hard.
    """.trimIndent(),
    decorator = decorator,
) {
    // TODO show code for each algorithm?
    // TODO do i need to show some pseudo code for MagicText?
    // TODO do i need to show some code examples of MagicMove use?

    Title()

    // TODO don't plan on using the companion, again... :-(
    //  - just ate too much time from the rehearsal time
    //  - i'd rather spend that time on ore diff details
    //  - or looking at compose code?
    // TODO !!! rework the opening to present the same ideas but faster
    Opening()

    // TODO make highlighting truly dynamic based on sample
    // TODO use mono font for function names
    LineEnding(FirstSample, SecondSample)

    // TODO joke about myers-briggs?
    //  daughter: Isabel Briggs Myers
    //  mother: Katharine Cook Briggs
    MyersDiffChars(FirstSample, SecondSample)

    // TODO use keyframes to compress navigation to single advancement?
    // TODO actually implement the algorithm to get the true search path visualization
    EditGraph(start = "KotlinConf", end = "droidcon")

    MyersDiffWords(FirstSample, SecondSample, ThirdSample)

    // TODO talk about Hunt-Szymanski algorithm (LCS)?
    //  - https://www.raygard.net/2025/01/28/how-histogram-diff-works/
    //  - or jump straight to patience sorting: https://en.wikipedia.org/wiki/Patience_sorting#Algorithm_for_finding_a_longest_increasing_subsequence

    PatienceStart(SecondSample, ThirdSample)

    PatienceSort(
        cards = listOf(
            Card.Nine, Card.Four, Card.Six, Card.Queen,
            Card.Eight, Card.Seven, Card.Ace, Card.Five,
            Card.Ten, Card.Jack, Card.Three, Card.Two,
            Card.King,
        )
    )

    PatienceEnd(SecondSample, ThirdSample)

    Idea(SecondSample, ThirdSample)

    // TODO idea: expand LCS elements first before other elements
    // TODO maybe better idea: use patience but always perform step 3 globally?
    //  - https://bramcohen.livejournal.com/73318.html

    Samples()

    // TODO talk about improvements to MagicText
    //  - make it generic (not text specific)
    //  - check out DiffUtil for an alternative idea
    //  - benchmarks and optimizations

    // TODO include a reference to blog post series
    Closing()
}
