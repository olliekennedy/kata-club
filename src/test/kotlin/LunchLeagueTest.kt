import com.natpryce.hamkrest.allElements
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

    @Test
    fun `can get a voting log for a restaurant`() {
        val leagueTable = LeagueTable()

        leagueTable.vote("Canteen", 1)
        leagueTable.vote("Canteen", 10)
        leagueTable.vote("Canteen", 8)
        leagueTable.vote("Canteen", 6)

        assertThat(
            leagueTable.votingLogFor("Canteen"),
            List<Vote>::containsAll,
            listOf(Vote(1), Vote(10), Vote(8), Vote(6)),
        )
    }

//    TODO - add a voter name
}

data class Vote(val rating: Int)

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

    fun votingLogFor(restaurantName: String): List<Vote> {
        return votes.getOrElse(restaurantName) { listOf() }.map { Vote(it) }
    }
}

private fun MutableList<Int>.average(): Double = sum().div(size.toDouble()).roundedToOneDecimalPlace()

private fun Double.roundedToOneDecimalPlace(): Double = BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toDouble()

data class Entry(val name: String, val rating: Double)