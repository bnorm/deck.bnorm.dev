import assertk.assertThat
import assertk.assertions.hasSize
import dev.bnorm.assert.assert
import dev.bnorm.assert.assertSoftly
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

enum class Race {
    Hobbit,
    Human,
    Dunadan {
        override fun toString() = "Dúnadan"
    },
    Elf,
    Drawf,
    Maia,
}

data class Character(
    val name: String,
    val race: Race,
    val age: Int,
    val alive: Boolean,
) {
    override fun toString(): String {
        return name
    }
}

data object FellowshipOfTheRing {
    private val members = listOf(
        Character(name = "Frodo", race = Race.Hobbit, age = 50, alive = true),
        Character(name = "Sam", race = Race.Hobbit, age = 38, alive = true),
        Character(name = "Merry", race = Race.Hobbit, age = 36, alive = true),
        Character(name = "Pippin", race = Race.Hobbit, age = 28, alive = true),
        Character(name = "Gandalf", race = Race.Maia, age = 10_000, alive = true),
        Character(name = "Aragorn", race = Race.Dunadan, age = 87, alive = true),
        Character(name = "Legolas", race = Race.Elf, age = 2_931, alive = true),
        Character(name = "Gimli", race = Race.Drawf, age = 139, alive = true),
        Character(name = "Boromir", race = Race.Human, age = 40, alive = false),
    )

    fun getAllMembers(): List<Character> = members
    fun getCurrentMembers(): List<Character> = members.filter { it.alive }

    operator fun get(index: Int): Character {
        require(index in members.indices)
        return members[index]
    }
}

internal class FellowshipOfTheRingTest {

    private val fellowshipOfTheRing = FellowshipOfTheRing

    @Test
    fun `test members of the fellowship1`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertTrue(members.size == 9)
    }

    @Test
    fun `test members of the fellowship2`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size)
    }

    @Test
    fun `test members of the fellowship3`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertEquals(9, members.size, "Members: $members")
    }

    @Test
    fun `test members of the fellowship4`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertThat(members).hasSize(9)
    }

    @Test
    fun `test members of the fellowship5`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assert(members.size == 9)
    }

    @Test
    fun `test members of the fellowship6`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assert(members.any { it.name == "Boromir" } &&
                members.any { it.name == "Aragorn" } ||
                members.any { it.name == "Elrond" })
    }

    @Test
    fun `test members of the fellowship7`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        val aragorn = members.find { it.name == "Aragorn" }
        assertTrue(aragorn != null)
        assertTrue(aragorn.age < 60)
    }

    @Test
    fun `test members of the fellowship8`() {
        fellowshipOfTheRing[10]
    }

    @Test
    fun `test members of the fellowship9`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        val aragorn = members.find { it.name == "Aragorn" }
        val boromir = members.find { it.name == "Boromir" }
        assertEquals(aragorn?.race, boromir?.race)
    }

    @Test
    fun `test members of the fellowship10`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        val aragorn = assertNotNull(members.find { it.name == "Aragorn" })
        val boromir = assertNotNull(members.find { it.name == "Boromir" })
        assertEquals(aragorn.race, boromir.race)
    }

    @Test
    fun `test members of the fellowship11`() {
        val members = fellowshipOfTheRing.getCurrentMembers()
        assertSoftly {
            assert(members.find { it.name == "Frodo" }?.age == 23)
            assert(members.find { it.name == "Aragorn" }?.age == 60)
        }
    }
}
