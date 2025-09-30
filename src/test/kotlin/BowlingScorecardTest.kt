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
    @ValueSource(ints = [1,2,3,4,5,6,7,8,9])
    fun `ivan improves`(score: Int) {
        assertEquals(score, scoreFor("$score- -- -- -- -- -- -- -- -- ---"))
    }

    private fun scoreFor(scorecard: String): Int {
        if(scorecard[0].isDigit()) return scorecard[0].digitToInt()
        return 0
    }
}
