import assertk.assertThat
import assertk.assertions.hasSize
import kotlin.test.*

class Sample {
    fun operation(): List<Int> = listOf(1, 2, 3)
}

internal class SampleTest {

    private val subject = Sample()

    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
        assertThat(actual).hasSize(4)
    }
}