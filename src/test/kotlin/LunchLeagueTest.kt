import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lunchleague.Entry
import lunchleague.LeagueTable
import lunchleague.Vote
import org.junit.jupiter.api.Test

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
