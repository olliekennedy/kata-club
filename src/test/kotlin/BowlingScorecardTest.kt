import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.text.contains

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
    fun `turkey in final frame`() {
        assertEquals(30, scoreFor("-- -- -- -- -- -- -- -- -- XXX"))
    }

    @Test
    fun `two bonuses from final frame`() {
        assertEquals(40, scoreFor("-- -- -- -- -- -- -- -- X- XX-"))
    }

    @Test
    fun `random game`() {
        assertEquals(166, scoreFor("52 19 81 X. X. 82 54 X. 91 X91"))
    }

    @Test
    fun `handles strike notation`() {
        assertEquals(166, scoreFor("52 19 81 X. X. 82 54 X. 91 X91"))
    }

    @Test
    fun `handles spare notation`() {
        assertEquals(152, scoreFor("52 1/ 81 X. X. 8/ 54 X. 9/ 1/5"))
    }

    private fun scoreFor(scorecard: String): Int =
        scorecard
            .split(" ")
            .replaceSpareNotationsWithRealNumbers()
            .let { frames ->
                frames.indices.map { index ->
                    Frame(
                        index = index,
                        currentFrame = frames[index],
                        nextFrame = frames.getOrNull(index + 1),
                        secondNextFrame = frames.getOrNull(index + 2),
                    )
                }
                    .sumOf { it.totalScore() }
            }

    data class Frame(
        val index: Int,
        val currentFrame: String,
        val nextFrame: String?,
        val secondNextFrame: String?,
    ) {
        fun totalScore() = simpleScore + nextBowlBonus() + secondNextBowlBonus()

        private val simpleScore = currentFrame.sumOf { scoreForBowl(it) }
        private val atLeastASpare = simpleScore == 10
        private fun String?.isAStrike(): Boolean = this != null && contains("X")

        private fun nextBowlBonus(): Int {
            if (!atLeastASpare) return 0

            return nextFrame?.first()?.let { scoreForBowl(it) } ?: 0
        }

        private fun secondNextBowlBonus(): Int {
            if (!currentFrame.isAStrike()) return 0

            return when {
                nextFrame.isAStrike() -> secondNextFrame?.first()?.let { scoreForBowl(it) } ?: 0
                else -> nextFrame?.getOrNull(1)?.let { scoreForBowl(it) } ?: 0
            }
        }

        private fun scoreForBowl(ch: Char): Int =
            when {
                ch.isDigit() -> ch.digitToInt()
                ch == 'X' -> 10
                else -> 0
            }
    }
}

private fun List<String>.replaceSpareNotationsWithRealNumbers() =
    this.map {
        it[0] + if (it[1] == '/') {
            (10 - it[0].digitToInt()).toString()
        } else {
            it[1].toString()
        } + (it.getOrNull(2) ?: "")
    }
