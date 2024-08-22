package dev.bnorm.kc25

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val githubInfo = GitHubInfo()

class GitHubInfo {
    private val scope = CoroutineScope(Job() + Dispatchers.Default)
    private val httpClient: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    var repositoryPath by mutableStateOf("bnorm/storyboard")
    var isError by mutableStateOf(false)
        private set
    var loading by mutableStateOf(false)
        private set
    var repository by mutableStateOf<Repository?>(null)
        private set

    init {
        scope.launch {
            snapshotFlow { repositoryPath }.collectLatest { path ->
                handlePath(path)
            }
        }
    }

    private suspend fun handlePath(path: String) {
        isError = false
        loading = true
        delay(500)

        val result = kotlin.runCatching {
            httpClient.get("https://api.github.com/repos/$path") {
            }.body<Repository>()
        }
        loading = false

        when {
            result.isSuccess -> {
                isError = false
                repository = result.getOrThrow()
            }

            result.isFailure -> {
                isError = true
            }
        }
    }
}

@Serializable
class Repository(
    val name: String,
    val forks_count: Long,
    val stargazers_count: Long,
    val subscribers_count: Long,
)
