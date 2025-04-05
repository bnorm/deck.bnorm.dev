package dev.bnorm.kc25.sections.plugin

import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import dev.bnorm.kc25.template.Body
import dev.bnorm.kc25.template.Header
import dev.bnorm.kc25.template.KodeeScene
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.toState

private val SAMPLES = buildCodeSamples {
    val ctor by tag("buildable annotated constructor")
    val defs by tag("")
    val body by tag("book class body")
    val propImpl by tag("")
    val prop by tag("")
    val build by tag("")

    val bookSample = extractTags(
        """
        class Book${ctor} @Buildable constructor${ctor}(
          val title: String,
          val series: String?${defs} = null${defs},
          val author: String,
          val publication: LocalDate,
        )${body} {
          class Builder {${propImpl}
            ${prop}private var title_flag: Boolean = false
            private var title_holder: String? = null${propImpl}
            var title: String${propImpl}
              get() = when {
                title_flag -> title_holder!!
                else -> throw IllegalStateException("Uninitialized property 'title'.")
              }
              set(value) {
                title_holder = value
                title_flag = true
              }${prop}
            ${propImpl}
            var series: String?
            var author: String
            var publication: LocalDate
    
            ${build}fun build(): Book${build}
          }
        }${body}
        """.trimIndent()
    )

    val whens by tag("")
    val bImpl by tag("")
    val title by tag("")
    val series by tag("")

    val buildSample = extractTags(
        """ 
        fun build(): Book${bImpl} = Book(
          ${title}title = when {
            title_flag -> title_holder!!
            else -> throw IllegalStateException("Uninitialized property 'title'.")
          }${title},
          ${series}series = when {
            series_flag -> series_holder!!
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
        """.trimIndent()
    )

    val bookStart = CodeSample { bookSample.toCode() }.hide(ctor, body, propImpl)
        .then { reveal(body).hide(defs) }
        .then { reveal(propImpl).focus(prop) }
        .then { hide(propImpl).unfocus() }
        .then { focus(build) }

    val buildSamples = CodeSample { buildSample.toCode() }.hide(bImpl).collapse(whens)
        .then { reveal(bImpl).focus(title) }
        .then { focus(series) }
        .then { unfocus().reveal(whens).hide(bImpl).collapse(whens) } // TODO reveal+collapse whens to place at end

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

    KodeeScene(stateCount = endExclusive - start) {
        Header()
        Body {
            ProvideTextStyle(MaterialTheme.typography.code1) {
                val text = frame.createChildTransition {
                    SAMPLES[start + it.toState()].get().toWords()
                }
                MagicText(text)
            }
        }
    }
}
