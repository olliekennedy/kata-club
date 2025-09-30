import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class BowlingScorecardTest {
    @Test
    fun `can score a gutterball game`() {
        assertEquals(0, scoreFor("-- -- -- -- -- -- -- -- -- --."))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4, 5, 6, 7, 8, 9])
    fun `can score a frame where only one throw is a hit`(score: Int) {
        assertEquals(score, scoreFor("$score- -- -- -- -- -- -- -- -- --."))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4])
    fun `can score a frame where both throws are a hit`(score: Int) {
        assertEquals(score * 2, scoreFor("$score$score -- -- -- -- -- -- -- -- --."))
    }

    @Test
    fun `not just beginners luck`() {
        assertEquals(14, scoreFor("-3 -- 2- -- 6- -- -- -- 21 --."))
    }

    @Test
    fun `spare on the last`() {
        assertEquals(16, scoreFor("-- -- -- -- -- -- -- -- -- 193"))
    }

    @Test
    fun `spares all over the shop`() {
        assertEquals(57, scoreFor("82 2- 82 -- -- 73 -7 -- -- 193"))
    }

    private fun scoreFor(scorecard: String): Int {
        val frames = scorecard.split(" ")
        return frames.mapIndexed { index, frame -> simpleScoreForFrame(frame) + if(simpleScoreForFrame(frame) == 10) scoreForNextBowl(frames, index) else 0 }.sum() + scoreForBowl(scorecard.last())
    }

    private fun scoreForNextBowl(frames: List<String>, index: Int): Int =
        if (index == frames.size - 1)
            0
        else
            scoreForBowl(frames[index + 1].first())

    private fun simpleScoreForFrame(frame: String): Int = frame.map { scoreForBowl(it) }.sum()

    private fun scoreForBowl(ch: Char): Int = if (ch.isDigit()) ch.digitToInt() else 0
}
