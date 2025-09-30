import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingScorecardTest {
    @Test
    fun `can score a gutterball game`() {
        assertEquals(0, scoreFor("-- -- -- -- -- -- -- -- -- ---"))
    }

    private fun scoreFor(scorecard: String): Int {
        return 0
    }
}
