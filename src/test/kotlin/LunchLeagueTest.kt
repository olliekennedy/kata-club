import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

class LunchLeagueTest {

    @Test
    fun `returns an empty league table`() {
        val leagueTable = LeagueTable()

        assertThat(leagueTable.results(), equalTo(listOf()))
    }

    @Test
    fun `can add votes for a restaurant`() {
        val leagueTable = LeagueTable()

        leagueTable.vote("Canteen", 8)
        leagueTable.vote("Canteen", 6)

        assertThat(leagueTable.results(), equalTo(listOf(Entry("Canteen", 7.0))))
    }

    @Test
    fun `can add multiple votes for a restaurant`() {
        val leagueTable = LeagueTable()

        leagueTable.vote("Canteen", 1)
        leagueTable.vote("Canteen", 10)
        leagueTable.vote("Canteen", 8)
        leagueTable.vote("Canteen", 6)

        assertThat(leagueTable.results(), equalTo(listOf(Entry("Canteen", 6.3))))
    }

//    TODO - add a voter name
}

class LeagueTable() {
    fun vote(name: String, rating: Int) {
        if (name !in votes) {
            votes[name] = mutableListOf(rating)
        } else {
            votes[name] = (mutableListOf(rating) + votes[name]!!).toMutableList()
        }
    }

    val votes = mutableMapOf<String, MutableList<Int>>()

    fun results(): List<Any> =
        votes.entries.map { (name, votes) ->
            Entry(name = name, rating = votes.average())
        }.toList()
}

private fun MutableList<Int>.average(): Double = sum().div(size.toDouble()).roundedToOneDecimalPlace()

private fun Double.roundedToOneDecimalPlace(): Double = BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toDouble()

data class Entry(val name: String, val rating: Double)