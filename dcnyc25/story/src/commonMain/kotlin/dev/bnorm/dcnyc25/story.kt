package dev.bnorm.dcnyc25

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.dcnyc25.sections.Opening
import dev.bnorm.dcnyc25.sections.*
import dev.bnorm.dcnyc25.template.Full
import dev.bnorm.dcnyc25.template.storyDecorator
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit

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
    Title()

    Opening()

    LineEnding()

    MyersDiffChars()

    EditGraph()

    MyersDiffWords()

    Patience()

    scene(
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Full(MaterialTheme.colors.primary) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Fin", style = MaterialTheme.typography.h1)
            }
        }
    }
}
