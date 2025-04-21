package dev.bnorm.kc25.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.kc25.sections.write.analyze.validateCheckerExtensionSample
import dev.bnorm.kc25.sections.write.analyze.validateCheckerSample
import dev.bnorm.kc25.sections.write.analyze.validateErrorsSample
import dev.bnorm.kc25.sections.write.resolve.validateFirGenerationSample
import dev.bnorm.kc25.sections.write.transform.validateIrGenerationSample
import dev.bnorm.kc25.sections.write.transform.validateVisitorSample
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

internal expect val VALIDATION: Boolean
    inline get

private var validated = false

@Composable
internal fun validateSample(sample: AnnotatedString, file: String) {
    val scope = rememberCoroutineScope()
    remember(file) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            val expected = Res.readBytes("files/samples/$file").decodeToString()
            require(expected == sample.text) {
                "\n$file\n----------\n$expected\n----------\n${sample.text}\n"
            }
        }
    }
}

@Composable
internal fun validateAllSamples() {
    if (!validated && VALIDATION) {
        validateFirGenerationSample()
        validateCheckerSample()
        validateCheckerExtensionSample()
        validateErrorsSample()
        validateIrGenerationSample()
        validateVisitorSample()
        validated = true
    }
}
