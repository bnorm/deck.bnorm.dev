package dev.bnorm.kc24.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalTextApi::class)
@Composable
fun TextWithLink(text: AnnotatedString) {
    val uriHandler = LocalUriHandler.current
    val layoutResult = remember<MutableState<TextLayoutResult?>> { mutableStateOf(null) }
    val pressIndicator = Modifier.pointerInput(key1 = text) {
        detectTapGestures { pos ->
            layoutResult.value?.let<TextLayoutResult, Unit> { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                text.getUrlAnnotations(offset, offset).firstOrNull()?.let {
                    uriHandler.openUri(it.item.url)
                }
            }
        }
    }

    Text(
        text = text,
        modifier = pressIndicator,
        onTextLayout = {
            layoutResult.value = it
        }
    )
}

@OptIn(ExperimentalTextApi::class)
fun AnnotatedString.Builder.appendLink(text: String, link: String = text) {
    val start = length
    append(text)
    val end = length

    addUrlAnnotation(UrlAnnotation(link), start, end)
    addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
}
