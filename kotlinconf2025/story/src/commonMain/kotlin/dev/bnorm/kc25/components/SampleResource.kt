package dev.bnorm.kc25.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.bnorm.deck.kotlinconf2025.generated.resources.Res
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
