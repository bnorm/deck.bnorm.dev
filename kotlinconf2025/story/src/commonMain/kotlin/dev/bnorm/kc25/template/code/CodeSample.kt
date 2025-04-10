package dev.bnorm.kc25.template.code

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.TextTagScope
import dev.bnorm.storyboard.text.addStyleByTag
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.replaceAllByTag

@Immutable
class CodeSample private constructor(
    private val base: AnnotatedString,
    private val focus: TextTag?,
    private val replaced: Map<TextTag, AnnotatedString>,
    private val styled: Map<TextTag, SpanStyle>,
    private val scrollTag: TextTag?,
    val data: Any?,
) {
    constructor(sample: AnnotatedString) : this(sample, null, emptyMap(), emptyMap(), null, null)

    val string: AnnotatedString by lazy {
        var str = base
        for ((tag, style) in styled) {
            str = str.addStyleByTag(tag, tagged = style)
        }
        if (focus != null) {
            str = str.addStyleByTag(focus, untagged = UNFOCUSED_STYLE)
        }
        // TODO collapse first and then hide?
        //  - will keep the collapses from being split,
        //    which results in adjacent collapses
        //  - or maybe the replaceAll can manage this by merging them together?
        for ((tag, replacement) in replaced) {
            str = str.replaceAllByTag(tag, replacement)
        }
        str
    }

    val scroll: Int by lazy {
        if (scrollTag == null) return@lazy 0

        val offset = string.getStringAnnotations(scrollTag.annotationStringTag, 0, string.length)
            .filter { it.item == scrollTag.id }
            .minOfOrNull { it.start } ?: 0

        string.text.substring(0, offset).count { it == '\n' }
    }

    companion object {
        private val UNFOCUSED_STYLE = SpanStyle(color = Color(0xFF555555))
        private val ELLIPSIS = AnnotatedString(" … ", spanStyle = UNFOCUSED_STYLE)
        private val EMPTY = AnnotatedString("")
    }

    private fun copy(
        base: AnnotatedString = this.base,
        focus: TextTag? = this.focus,
        replaced: Map<TextTag, AnnotatedString> = this.replaced,
        styled: Map<TextTag, SpanStyle> = this.styled,
        scrollTag: TextTag? = this.scrollTag,
        data: Any? = this.data,
    ): CodeSample = CodeSample(base, focus, replaced, styled, scrollTag, data)

    fun collapse(tag: TextTag): CodeSample = copy(replaced = replaced + (tag to ELLIPSIS))
    fun collapse(vararg tags: TextTag): CodeSample = collapse(tags.asList())
    fun collapse(tags: List<TextTag>): CodeSample {
        if (tags.isEmpty()) return this
        return copy(replaced = replaced + tags.map { it to ELLIPSIS })
    }

    fun hide(tag: TextTag): CodeSample = copy(replaced = replaced + (tag to EMPTY))
    fun hide(vararg tags: TextTag): CodeSample = hide(tags.asList())
    fun hide(tags: List<TextTag>): CodeSample {
        if (tags.isEmpty()) return this
        return copy(replaced = replaced + tags.map { it to EMPTY })
    }

    fun reveal(tag: TextTag): CodeSample = copy(replaced = replaced - tag)
    fun reveal(vararg tags: TextTag): CodeSample = reveal(tags.asList())
    fun reveal(tags: List<TextTag>): CodeSample {
        if (tags.isEmpty()) return this
        return copy(replaced = replaced - tags)
    }

    fun focus(tag: TextTag, scroll: Boolean = true): CodeSample =
        copy(focus = tag, scrollTag = if (scroll) tag else null)

    fun unfocus(unscroll: Boolean = true): CodeSample =
        copy(focus = null, scrollTag = if (unscroll) null else scrollTag)

    fun styled(tag: TextTag, style: SpanStyle): CodeSample = copy(styled = styled + (tag to style))
    fun unstyled(tag: TextTag): CodeSample = copy(styled = styled - tag)

    fun scroll(tag: TextTag?): CodeSample = copy(scrollTag = tag)

    fun attach(data: Any): CodeSample {
        if (this.data == data) return this
        return copy(data = data)
    }

    fun detach(): CodeSample {
        if (data == null) return this
        return copy(data = null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CodeSample

        if (base != other.base) return false
        if (focus != other.focus) return false
        if (replaced != other.replaced) return false
        if (styled != other.styled) return false
        if (scrollTag != other.scrollTag) return false
        if (data != other.data) return false
        return true
    }

    override fun hashCode(): Int {
        var result = base.hashCode()
        result = 31 * result + (focus?.hashCode() ?: 0)
        result = 31 * result + replaced.hashCode()
        result = 31 * result + styled.hashCode()
        result = 31 * result + (scrollTag?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }
}

fun buildCodeSamples(builder: CodeSamplesBuilder.() -> List<CodeSample>): List<CodeSample> =
    CodeSamplesBuilder().builder()

class CodeSamplesBuilder : TextTagScope.Default() {
    fun String.toCodeSample(
        codeStyle: CodeStyle,
        scope: CodeScope = CodeScope.File,
        identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
    ): CodeSample {
        return CodeSample(extractTags(this).toCode(codeStyle, scope, identifierType))
    }

    fun CodeSample.collapse(data: Any?): CodeSample = collapse(tags.filter { data == it.data })
    fun CodeSample.hide(data: Any?): CodeSample = hide(tags.filter { data == it.data })
    fun CodeSample.reveal(data: Any?): CodeSample = reveal(tags.filter { data == it.data })

    fun CodeSample.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return listOf(this, transformer(this))
    }

    fun List<CodeSample>.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this + transformer(this.last())
    }
}
