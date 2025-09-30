import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class BowlingScorecardTest {
    @Test
    fun `can score a gutterball game`() {
        assertEquals(0, scoreFor("-- -- -- -- -- -- -- -- -- ---"))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4, 5, 6, 7, 8, 9])
    fun `can score a frame where only one throw is a hit`(score: Int) {
        assertEquals(score, scoreFor("$score- -- -- -- -- -- -- -- -- ---"))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4])
    fun `can score a frame where both throws are a hit`(score: Int) {
        assertEquals(score * 2, scoreFor("$score$score -- -- -- -- -- -- -- -- ---"))
    }

    @Test
    fun `not just beginners luck`() {
        assertEquals(14, scoreFor("-3 -- 2- -- 6- -- -- -- 21 ---"))
    }

    private fun scoreFor(scorecard: String): Int {
        return scorecard.map { if(it.isDigit()) it.digitToInt() else 0 }.sum()
    }
}
