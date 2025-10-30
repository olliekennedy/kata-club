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

        leagueTable.vote("Canteen", "me", 8)
        leagueTable.vote("Canteen", "bob", 6)

        assertThat(leagueTable.results(), equalTo(listOf(Entry("Canteen", 7.0))))
    }

    @Test
    fun `can add multiple votes for a restaurant`() {
        val leagueTable = LeagueTable()

        leagueTable.vote("Canteen", "me", 1)
        leagueTable.vote("Canteen", "bob", 10)
        leagueTable.vote("Canteen", "sandra", 8)
        leagueTable.vote("Canteen", "brenda", 6)

        assertThat(leagueTable.results(), equalTo(listOf(Entry("Canteen", 6.3))))
    }

    @Test
    fun `vote for multiple restaurants`() {
        val leagueTable = LeagueTable()

        leagueTable.vote("Canteen", "me", 6)
        leagueTable.vote("Canteen", "bob", 8)
        leagueTable.vote("Randy's Wing Bar", "me", 8)
        leagueTable.vote("Hackney Bridge", "me", 6)

        assertThat(leagueTable.results(), equalTo(listOf(Entry("Canteen", 7.0), Entry("Randy's Wing Bar", 8.0), Entry("Hackney Bridge", 6.0))))
        assertThat(
            leagueTable.votingLogFor("Canteen"),
            List<Vote>::containsAll,
            listOf(Vote("me", 6), Vote("bob", 8)),
        )
        assertThat(leagueTable.votingLogFor("Randy's Wing Bar"), equalTo(listOf(Vote("me", 8))))
        assertThat(leagueTable.votingLogFor("Hackney Bridge"), equalTo(listOf(Vote("me", 6))))
    }

    @Test
    fun `someone can update their rating for a restaurant`() {
        val leagueTable = LeagueTable()

        leagueTable.vote("Canteen", "me", 6)
        leagueTable.vote("Canteen", "me", 8)

        assertThat(leagueTable.votingLogFor("Canteen"), equalTo(listOf(Vote("me", 8))))
    }
}

data class Vote(val voter: String, val rating: Int)

class LeagueTable() {
    fun vote(restaurant: String, voter: String, rating: Int) {
        if (restaurant !in votes) {
            votes[restaurant] = mutableMapOf(voter to rating)
        } else {
            votes[restaurant]?.set(voter, rating)
        }
    }

    val votes = mutableMapOf<String, MutableMap<String, Int>>()

    fun results(): List<Any> =
        votes.entries.map { (name, votes) ->
            Entry(name = name, rating = votes.map { it.value }.average())
        }.toList()

    fun votingLogFor(restaurantName: String): List<Vote> {
        return votes.getOrElse(restaurantName) { mapOf() }.map { Vote(it.key, it.value) }
    }
}

private fun List<Int>.average(): Double = sum().div(size.toDouble()).roundedToOneDecimalPlace()

private fun Double.roundedToOneDecimalPlace(): Double = BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toDouble()

data class Entry(val name: String, val rating: Double)