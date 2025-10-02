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
        assertEquals(13, scoreFor("-- -- -- -- -- -- -- -- -- 193"))
    }

    @Test
    fun `spares all over the shop`() {
        assertEquals(54, scoreFor("82 2- 82 -- -- 73 -7 -- -- 193"))
    }

    @Test
    fun `spare game`() {
        assertEquals(190, scoreFor("91 91 91 91 91 91 91 91 91 919"))
    }

    @Test
    fun `strike that never materialised`() {
        assertEquals(10, scoreFor("X. -- -- -- -- -- -- -- -- --."))
    }

    @Test
    fun `strike that materialises slightly`() {
        assertEquals(20, scoreFor("X. 5- -- -- -- -- -- -- -- --."))
    }

    @Test
    fun `strike that completely materialises`() {
        assertEquals(28, scoreFor("X. 54 -- -- -- -- -- -- -- --."))
    }

    @Test
    fun `two strikes in a row`() {
        assertEquals(39, scoreFor("X. X. 3- -- -- -- -- -- -- --."))
    }

    @Test
    fun `random game`() {
        assertEquals(166, scoreFor("52 19 81 X. X. 82 54 X. 91 X91"))
    }

    @Test
    fun `turkey in final frame`() {
        assertEquals(30, scoreFor("-- -- -- -- -- -- -- -- -- XXX"))
    }

    private fun scoreFor(scorecard: String): Int {
        val frames = scorecard.split(" ")
        val mapIndexed = frames.mapIndexed { index, frame ->
            frame.simpleScore().let {
                it + if (frame.isAStrike()) {
                    calculateBonusesForStrike(frames, index)
                } else if (it.isASpare()) {
                    scoreForNextBowl(frames, index)
                } else {
                    0
                }
            }
        }
        return mapIndexed.sum()
    }

    private fun calculateBonusesForStrike(frames: List<String>, index: Int): Int =
            if (frames.getOrNull(index + 1) != null && frames[index + 1].isAStrike())
                scoreForNextBowl(frames, index) + scoreForNextBowl(frames, index + 1)
            else
                scoreForNextBowl(frames, index) + scoreForSecondNextBowl(frames, index)

    private fun Int.isASpare(): Boolean = this == 10

    private fun String.isAStrike(): Boolean = this.contains("X")

    private fun scoreForNextBowl(frames: List<String>, index: Int): Int =
        if (index == frames.size - 1)
            0
        else
            scoreForBowl(frames[index + 1].first())

    private fun scoreForSecondNextBowl(frames: List<String>, index: Int): Int =
        if (index == frames.size - 1)
            0
        else
            scoreForBowl(frames[index + 1][1])

    private fun String.simpleScore(): Int = map { scoreForBowl(it) }.sum()

    private fun scoreForBowl(ch: Char): Int =
        when {
            ch.isDigit() -> ch.digitToInt()
            ch == 'X' -> 10
            else -> 0
        }
}
