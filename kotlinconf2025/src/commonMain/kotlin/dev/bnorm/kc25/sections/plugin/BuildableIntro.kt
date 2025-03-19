package dev.bnorm.kc25.sections.plugin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.HeaderAndBody
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.text.magic.MagicText

private val BOOK = """
class Book(
  val title: String,
  val series: String? = null,
  val author: String,
  val publication: LocalDate,
)
""".trimIndent()

private val BOOK_WITH_BUILDER = """
class Book private constructor(
  val title: String,
  val series: String?,
  val author: String,
  val publication: LocalDate,
) {
  class Builder {
    var title: String
    var series: String?
    var author: String
    var publication: LocalDate

    fun build(): Book
  }
}
""".trimIndent()

private val BOOK_WITH_BUILDER_IMPL = """
class Book private constructor(
  val title: String,
  val series: String?,
  val author: String,
  val publication: LocalDate,
) {
  class Builder {
    private companion object {
      private const val FLAG_TITLE = 0b0001
      private const val FLAG_SERIES = 0b0010
      private const val FLAG_AUTHOR = 0b0100
      private const val FLAG_PUBLICATION = 0b1000
    }

    private var initialized = 0

    private var titleHolder: String? = null
    var title: String
      get() = when {
        initialized and FLAG_TITLE != 0 -> titleHolder!!
        else -> throw IllegalStateException("Uninitialized property 'title'.")
      }
      set(value) {
        titleHolder = value
        initialized = initialized or FLAG_TITLE
      }

    private var seriesHolder: String? = null
    var series: String?
      get() = when {
        initialized or FLAG_SERIES != 0 -> seriesHolder
        else -> throw IllegalStateException("Uninitialized property 'series'.")
      }
      set(value) {
        seriesHolder = value
        initialized = initialized and FLAG_SERIES
      }

    private var authorHolder: String? = null
    var author: String
      get() = when {
        initialized and FLAG_AUTHOR != 0 -> authorHolder!!
        else -> throw IllegalStateException("Uninitialized property 'author'.")
      }
      set(value) {
        authorHolder = value
        initialized = initialized or FLAG_AUTHOR
      }

    private var publicationHolder: LocalDate? = null
    var publication: LocalDate
      get() = when {
        initialized and FLAG_PUBLICATION != 0 -> publicationHolder!!
        else -> throw IllegalStateException("Uninitialized property 'publication'.")
      }
      set(value) {
        publicationHolder = value
        initialized = initialized or FLAG_PUBLICATION
      }

    fun build(): Book {
      return Book(
        title = when {
          initialized and FLAG_TITLE != 0 -> titleHolder!!
          else -> throw IllegalStateException("Uninitialized property 'title'.")
        },
        series = when {
          initialized or FLAG_SERIES != 0 -> seriesHolder!!
          else -> null
        },
        author = when {
          initialized and FLAG_AUTHOR != 0 -> authorHolder!!
          else -> throw IllegalStateException("Uninitialized property 'author'.")
        },
        publication = when {
          initialized and FLAG_PUBLICATION != 0 -> publicationHolder!!
          else -> throw IllegalStateException("Uninitialized property 'publication'.")
        },
      )
    }
  }
}
""".trimIndent()

private val BUILDABLE_BOOK = """
class Book @Buildable constructor(
  val title: String,
  val series: String? = null,
  val author: String,
  val publication: LocalDate,
)
""".trimIndent()

// TODO slightly more complex example with...
//  - primitives
//  - referencing previous arguments in a default argument
fun StoryboardBuilder.BuildableIntro() {
    slide(
        stateCount = 5
    ) {
        HeaderAndBody {
            val sample = when (currentState) {
                0 -> BOOK.toCode()
                1 -> BOOK_WITH_BUILDER.toCode()
                2 -> BOOK_WITH_BUILDER_IMPL.toCode()
                3 -> BOOK.toCode()
                4 -> BUILDABLE_BOOK.toCode()
                else -> "".toCode()
            }

            val verticalScrollState = rememberScrollState()
            Box(modifier = Modifier.fillMaxSize().verticalScroll(verticalScrollState).padding(32.dp)) {
                MagicText(sample)
            }
        }
    }
}