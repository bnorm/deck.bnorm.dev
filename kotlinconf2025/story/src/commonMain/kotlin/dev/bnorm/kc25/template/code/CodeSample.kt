package dev.bnorm.kc25.template.code

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.addStyleByTag
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.replaceAllByTag
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Immutable
class CodeSample private constructor(
    private val base: AnnotatedString,
    private val focus: TextTag?,
    private val replaced: Map<TextTag, AnnotatedString>,
    private val styled: Map<TextTag, SpanStyle>,
) {
    constructor(sample: AnnotatedString) : this(sample, null, emptyMap(), emptyMap())

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
    ): CodeSample = CodeSample(base, focus, replaced, styled)

    fun collapse(tag: TextTag): CodeSample = copy(replaced = replaced + (tag to ELLIPSIS))
    fun collapse(vararg tags: TextTag): CodeSample = copy(replaced = replaced + tags.map { it to ELLIPSIS })

    fun hide(tag: TextTag): CodeSample = copy(replaced = replaced + (tag to EMPTY))
    fun hide(vararg tags: TextTag): CodeSample = copy(replaced = replaced + tags.map { it to EMPTY })

    fun reveal(tag: TextTag): CodeSample = copy(replaced = replaced - tag)
    fun reveal(vararg tags: TextTag): CodeSample = copy(replaced = replaced - tags)

    fun focus(tag: TextTag): CodeSample = copy(focus = tag)
    fun unfocus(): CodeSample = copy(focus = null)

    fun styled(tag: TextTag, style: SpanStyle): CodeSample = copy(styled = styled + (tag to style))
    fun unstyled(tag: TextTag): CodeSample = copy(styled = styled - tag)

    fun get(): AnnotatedString {
        return string
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CodeSample

        if (base != other.base) return false
        if (focus != other.focus) return false
        if (replaced != other.replaced) return false
        return true
    }

    override fun hashCode(): Int {
        var result = base.hashCode()
        result = 31 * result + (focus?.hashCode() ?: 0)
        result = 31 * result + replaced.hashCode()
        return result
    }
}

fun AnnotatedString.toCodeSample(): CodeSample = CodeSample(this)

fun buildCodeSamples(builder: CodeSamplesBuilder.() -> List<CodeSample>): List<CodeSample> =
    CodeSamplesBuilder.Default.builder()

sealed class CodeSamplesBuilder {
    internal companion object Default : CodeSamplesBuilder()

    private val tags = mutableListOf<TextTag>()

    fun CodeSample.collapseAll(): CodeSample = collapse(*tags.toTypedArray())
    fun CodeSample.hideAll(): CodeSample = hide(*tags.toTypedArray())
    fun CodeSample.revealAll(): CodeSample = reveal(*tags.toTypedArray())

    private inner class TagProvider(
        val description: String,
    ) : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, TextTag>> {
        override operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, TextTag> {
            val tag = TextTag(property.name, description, null)
            tags.add(tag)
            return object : ReadOnlyProperty<Any?, TextTag> {
                override fun getValue(thisRef: Any?, property: KProperty<*>): TextTag = tag
            }
        }
    }

    fun extractTags(string: String): AnnotatedString {
        return TextTag.extractTags(string)
    }

    fun String.toCodeSample(
        codeStyle: CodeStyle,
        identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
    ): CodeSample {
        return extractTags(this).toCode(codeStyle, identifierType).toCodeSample()
    }

    fun tag(description: String): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, TextTag>> =
        TagProvider(description)

    fun CodeSample.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return listOf(this, transformer(this))
    }

    fun List<CodeSample>.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this + transformer(this.last())
    }
}
