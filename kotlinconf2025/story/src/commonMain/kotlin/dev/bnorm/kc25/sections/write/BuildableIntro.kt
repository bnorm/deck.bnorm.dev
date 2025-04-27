package dev.bnorm.kc25.sections.write

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.components.MagicCodeSample
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState

private val SAMPLES = buildCodeSamples {
    val ctor by tag("buildable annotated constructor")
    val defs by tag("")
    val body by tag("book class body")
    val builder by tag("book builder")
    val propImpl by tag("")
    val prop by tag("")
    val build by tag("")

    val whens by tag("")
    val bImpl by tag("")
    val title by tag("")
    val series by tag("")

    val bookSample = """
        class Book${ctor} @Buildable constructor${ctor}(
          val title: String,
          val series: String?${defs} = null${defs},
          val author: String,
          val publication: LocalDate,
        )${body} {
          ${builder}class Builder {${prop}${propImpl}
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
    
            ${build}fun build(): Book = Book(${bImpl}
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
            ${bImpl})${build}
          }${builder}
        }${body}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    bookSample.hide(ctor, body, propImpl).collapse(bImpl, whens)
        .then { reveal(body).hide(defs).focus(builder, scroll = false) }
        .then { reveal(propImpl).focus(prop).scroll(builder) }
        .then { hide(propImpl).focus(build).scroll(builder) }
        .then { reveal(bImpl).focus(title).scroll(build) }
        .then { focus(series).scroll(build) }
        .then { focus(build).reveal(whens) }
        .then { collapse(bImpl).unfocus() }
        .then { hide(bImpl, body).reveal(defs) }
        .then { reveal(ctor) }
}

fun StoryboardBuilder.BuildableIntro(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }
    sink.addAll(SAMPLES)

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        val sample = frame.createChildTransition { SAMPLES[start + it.toState()] }

        HeaderScaffold { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                sample.MagicCodeSample(modifier = Modifier.padding(padding))
            }
        }
    }
}
