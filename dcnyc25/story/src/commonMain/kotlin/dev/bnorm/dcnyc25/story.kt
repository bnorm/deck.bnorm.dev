package dev.bnorm.dcnyc25

import dev.bnorm.dcnyc25.sections.*
import dev.bnorm.dcnyc25.template.FirstSample
import dev.bnorm.dcnyc25.template.SecondSample
import dev.bnorm.dcnyc25.template.ThirdSample
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(
    decorator: SceneDecorator = storyDecorator(),
    includeTextFieldSamples: Boolean = false,
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
    // TODO need to run through this opening a couple times to make sure it works
    Opening()

    // TODO make line ending highlighting dynamic based on sample
    LineEnding(FirstSample, SecondSample)

    MyersDiffChars(FirstSample, SecondSample)

    // TODO actually implement the algorithm to get the true search path visualization
    EditGraph(start = "KotlinConf", end = "droidcon")

    MyersDiffWords(FirstSample, SecondSample, ThirdSample)

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

    // TODO make expansion highlighting dynamic based on sample
    Idea(SecondSample, ThirdSample)

    // TODO talk about other improvements to MagicText
    //  - make it generic (not text specific)
    //  - check out DiffUtil for an alternative idea
    //  - benchmarks and optimizations

    if (includeTextFieldSamples) {
        MagicTextField(SecondSample, ThirdSample)
    }

    MagicSamples()

    Closing()
}
