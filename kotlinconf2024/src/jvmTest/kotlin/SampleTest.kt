import kotlin.test.*

class Sample {
    fun operation(): List<Int> = listOf(1, 2, 3)
}

internal class SampleTest {

    private val subject = Sample()

    @Test
    fun test() {
        val actual: List<Int> = subject.operation()
        assertEquals(4, actual.size, "Actual: $actual")
    }
}