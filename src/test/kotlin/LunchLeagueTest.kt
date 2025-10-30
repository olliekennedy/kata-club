import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

fun main() {
    val leagueTable = LeagueTable()

    println("Welcome to Lunch League!")
    while (true) {
        println("\nChoose an option:")
        println("1. Vote")
        println("2. Show results")
        println("3. Show voting log for a restaurant")
        println("4. Exit")
        when (readLine()?.trim()) {
            "1" -> {
                print("Restaurant name: ")
                val restaurant = readLine()?.trim().orEmpty()
                print("Your name: ")
                val voter = readLine()?.trim().orEmpty()
                print("Your rating (1-10): ")
                val rating = readLine()?.trim()?.toIntOrNull() ?: 0
                leagueTable.vote(restaurant, voter, rating)
                println("Vote recorded.")
            }
            "2" -> {
                println("Leaderboard:")
                println(leagueTable.leaderboard())
            }
            "3" -> {
                print("Restaurant name: ")
                val restaurant = readLine()?.trim().orEmpty()
                val log = leagueTable.votingLogFor(restaurant)
                if (log.isEmpty()) {
                    println("No votes for $restaurant.")
                } else {
                    log.forEach { println("${it.voter}: ${it.rating}") }
                }
            }
            "4" -> {
                println("Goodbye!")
                break
            }
            else -> println("Invalid option.")
        }
    }
}

class LunchLeagueTest {
    val leagueTable = LeagueTable()

    @Test
    fun `can calculate average ratings for each restaurant`() {
        placeABunchOfVotes()

        assertThat(
            leagueTable.scores(),
            equalTo(
                listOf(
                    Entry("Canteen", 6.3),
                    Entry("Randy's Wing Bar", 2.5),
                    Entry("Hackney Bridge", 6.5),
                )
            )
        )
    }

    @Test
    fun `can get a voting log for a restaurant`() {
        placeABunchOfVotes()

        assertThat(
            leagueTable.votingLogFor("Canteen"),
            List<Vote>::containsAll,
            listOf(Vote("me", 1), Vote("bob", 10), Vote("sandra", 8), Vote("brenda", 6)),
        )
        assertThat(
            leagueTable.votingLogFor("Canteen"),
            List<Vote>::containsAll,
            listOf(Vote("me", 1), Vote("bob", 10), Vote("sandra", 8), Vote("brenda", 6)),
        )
        assertThat(
            leagueTable.votingLogFor("Canteen"),
            List<Vote>::containsAll,
            listOf(Vote("me", 1), Vote("bob", 10), Vote("sandra", 8), Vote("brenda", 6)),
        )
    }

    @Test
    fun `someone can update their rating for a restaurant`() {
        leagueTable.vote("Canteen", "me", 6)
        leagueTable.vote("Canteen", "me", 8)

        assertThat(leagueTable.votingLogFor("Canteen"), equalTo(listOf(Vote("me", 8))))
    }

    @Test
    fun `can get a nice leaderboard`() {
        placeABunchOfVotes()

        assertThat(
            leagueTable.leaderboard(),
            equalTo(
                """
                    1. Hackney Bridge 6.5
                    2. Canteen 6.3
                    3. Randy's Wing Bar 2.5
                """.trimIndent()
            )
        )
    }

    private fun placeABunchOfVotes() {
        leagueTable.vote("Canteen", "me", 1)
        leagueTable.vote("Canteen", "bob", 10)
        leagueTable.vote("Canteen", "sandra", 8)
        leagueTable.vote("Canteen", "brenda", 6)
        leagueTable.vote("Randy's Wing Bar", "me", 1)
        leagueTable.vote("Randy's Wing Bar", "bob", 2)
        leagueTable.vote("Randy's Wing Bar", "sandra", 3)
        leagueTable.vote("Randy's Wing Bar", "brenda", 4)
        leagueTable.vote("Hackney Bridge", "me", 3)
        leagueTable.vote("Hackney Bridge", "bob", 4)
        leagueTable.vote("Hackney Bridge", "sandra", 9)
        leagueTable.vote("Hackney Bridge", "brenda", 10)
    }
}

class LeagueTable() {
    val votes = mutableMapOf<String, MutableMap<String, Int>>()

    fun vote(restaurant: String, voter: String, rating: Int) {
        val restaurantVotes = votes.getOrPut(restaurant) { mutableMapOf() }
        restaurantVotes[voter] = rating
    }

    fun scores(): List<Entry> =
        votes.entries.map { (name, votes) ->
            val rating = votes.map { it.value }.average()
            Entry(name, rating)
        }.toList()

    fun votingLogFor(restaurantName: String): List<Vote> =
        votes
            .getOrElse(restaurantName) { mapOf() }
            .map { Vote(it.key, it.value) }

    fun leaderboard(): String =
        scores()
            .sortedByDescending { it.rating }
            .mapIndexed { i, it -> "${i + 1}. ${it.name} ${it.rating}" }
            .joinToString("\n")
}

private fun List<Int>.average(): Double = sum().div(size.toDouble()).roundedToOneDecimalPlace()
private fun Double.roundedToOneDecimalPlace(): Double = BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toDouble()

data class Entry(val name: String, val rating: Double)
data class Vote(val voter: String, val rating: Int)