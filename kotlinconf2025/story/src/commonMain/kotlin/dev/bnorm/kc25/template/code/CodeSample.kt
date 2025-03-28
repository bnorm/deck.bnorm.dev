package dev.bnorm.kc25.template.code

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.addStyleByTag
import dev.bnorm.storyboard.text.replaceAllByTag
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Immutable
class CodeSample private constructor(
    private val sample: @Composable () -> AnnotatedString,
    private val focus: TextTag?,
    private val replaced: Map<TextTag, AnnotatedString>,
    private val styled: Map<TextTag, SpanStyle>,
) {
    constructor(sample: @Composable () -> AnnotatedString) : this(sample, null, emptyMap(), emptyMap())

    private var cached: AnnotatedString? = null

    companion object {
        private val ELLIPSIS = AnnotatedString(" … ", spanStyle = SpanStyle(color = Color.Gray.copy(alpha = 0.5f)))
        private val EMPTY = AnnotatedString("")
        private val UNFOCUSED_STYLE = SpanStyle(color = Color.Gray.copy(alpha = 0.5f))
    }

    private fun copy(
        sample: @Composable () -> AnnotatedString = this.sample,
        focus: TextTag? = this.focus,
        replaced: Map<TextTag, AnnotatedString> = this.replaced,
        styled: Map<TextTag, SpanStyle> = this.styled,
    ): CodeSample = CodeSample(sample, focus, replaced, styled)

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

    @Composable
    fun get(): AnnotatedString {
        // Cache the result outside Compose since it never changes.
        cached?.let { return it }

        var str = sample()
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
        cached = str
        return str
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CodeSample

        if (sample != other.sample) return false
        if (focus != other.focus) return false
        if (replaced != other.replaced) return false
        return true
    }

    override fun hashCode(): Int {
        var result = sample.hashCode()
        result = 31 * result + (focus?.hashCode() ?: 0)
        result = 31 * result + replaced.hashCode()
        return result
    }
}

fun buildCodeSamples(builder: CodeSamplesBuilder.() -> List<CodeSample>): List<CodeSample> =
    CodeSamplesBuilder.Default.builder()

sealed interface CodeSamplesBuilder {
    companion object Default : CodeSamplesBuilder

    private object TagProvider : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, TextTag>> {
        override operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, TextTag> {
            val tag = TextTag(property.name)
            return object : ReadOnlyProperty<Any?, TextTag> {
                override fun getValue(thisRef: Any?, property: KProperty<*>): TextTag = tag
            }
        }
    }

    fun extractTags(string: String): AnnotatedString {
        return TextTag.extractTags(string)
    }

    fun tag(description: String): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, TextTag>> = TagProvider

    fun CodeSample.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return listOf(this, transformer(this))
    }

    fun List<CodeSample>.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this + transformer(this.last())
    }
}
