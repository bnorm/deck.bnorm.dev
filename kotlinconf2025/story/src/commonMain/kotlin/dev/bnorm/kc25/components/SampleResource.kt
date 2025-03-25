package dev.bnorm.kc25.components

import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.deck.story.generated.resources.Res
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

@Composable
fun sampleResource(file: String): State<String> {
    val scope = rememberCoroutineScope()
    return remember(file) {
        val mutableState = mutableStateOf("")
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            mutableState.value = Res.readBytes("files/samples/$file").decodeToString()
        }
        mutableState
    }
}

private const val VALIDATION = true

@Composable
fun validateSampleResource(sample: AnnotatedString, file: String): AnnotatedString {
    if (VALIDATION) {
        val expected by sampleResource(file)
        require(expected == sample.text)
    }

    return sample
}
