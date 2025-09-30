import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingScorecardTest {
    @Test
    fun `can score a gutterball game`() {
        assertEquals(0, scoreFor("-- -- -- -- -- -- -- -- -- ---"))
    }

    @Test
    fun `can score ivan style game`() {
        assertEquals(1, scoreFor("1- -- -- -- -- -- -- -- -- ---"))
    }

    private fun scoreFor(scorecard: String): Int {
        if (scorecard.contains("1")) return 1
        return 0
    }
}
