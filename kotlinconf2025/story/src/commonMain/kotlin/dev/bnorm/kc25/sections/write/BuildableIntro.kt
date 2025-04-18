package dev.bnorm.kc25.sections.write

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private val SAMPLES = buildCodeSamples {
    val ctor by tag("buildable annotated constructor")
    val defs by tag("")
    val body by tag("book class body")
    val propImpl by tag("")
    val prop by tag("")
    val build by tag("")

    val bookSample = """
        class Book${ctor} @Buildable constructor${ctor}(
          val title: String,
          val series: String?${defs} = null${defs},
          val author: String,
          val publication: LocalDate,
        )${body} {
          class Builder {${prop}${propImpl}
            private var title_flag: Boolean = false
            private var title_holder: String? = null${propImpl}
            var title: String${propImpl}
              get() = when {
                title_flag -> title_holder!!
                else -> throw IllegalStateException("Uninitialized property 'title'.")
              }
              set(value) {
                title_holder = value
                title_flag = true
              }
            ${propImpl}${prop}
            var series: String?
            var author: String
            var publication: LocalDate
    
            ${build}fun build(): Book${build}
          }
        }${body}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val whens by tag("")
    val bImpl by tag("")
    val title by tag("")
    val series by tag("")

    // TODO do this as part of the larger sample but with scrolling
    val buildSample = """ 
        fun build(): Book${bImpl} = Book(
          ${title}title = when {
            title_flag -> title_holder!!
            else -> throw IllegalStateException("Uninitialized property 'title'.")
          }${title},
          ${series}series = when {
            series_flag -> series_holder
            else -> null
          }${series},
          author = when {${whens}
            author_flag -> author_holder!!
            else -> throw IllegalStateException("Uninitialized property 'author'.")
          ${whens}},
          publication = when {${whens}
            publication_flag -> publication_holder!!
            else -> throw IllegalStateException("Uninitialized property 'publication'.")
          ${whens}},
        )${bImpl}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val bookStart = bookSample.hide(ctor, body, propImpl)
        .then { reveal(body).hide(defs) }
        .then { reveal(propImpl).focus(prop) }
        .then { hide(propImpl).unfocus() }
        .then { focus(build) }

    val buildSamples = buildSample.hide(bImpl).collapse(whens)
        .then { reveal(bImpl).focus(title) }
        .then { focus(series) }
        .then { unfocus().reveal(whens) }
        .then { hide(bImpl) }

    val bookEnd = bookStart.last().unfocus()
        .then { hide(body).reveal(defs) }
        .then { reveal(ctor) }

    bookStart + buildSamples + bookEnd
}

// TODO slightly more complex example with...
//  - primitives
//  - referencing previous arguments in a default argument
fun StoryboardBuilder.BuildableIntro(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        HeaderScaffold { padding ->
            Box(Modifier.padding(padding)) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    val text = frame.createChildTransition {
                        SAMPLES[start + it.toState()].string.splitByTags()
                    }
                    MagicText(text)
                }
            }
        }
    }
}
