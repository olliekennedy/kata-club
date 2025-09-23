import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChangeMakerTest {
    val changeMaker = ChangeMaker()

    @Test
    fun `returns correct change for small amount`() {
        val result = changeMaker.calculate(listOf(1), 1)

        assertEquals(listOf(1), result)
    }

    @Test
    fun `return correct change for two denomination`() {
        val result = changeMaker.calculate(listOf(1, 5), changeAmount = 20)

        assertEquals(listOf(0, 4), result)
    }

    @Test
    fun `return more complex change`() {
        val result = changeMaker.calculate(listOf(1, 5), changeAmount = 21)

        assertEquals(listOf(1, 4), result)
    }

    @Test
    fun `complex change three denomination`() {
        val result = changeMaker.calculate(listOf(1, 5, 10), 33)
        assertEquals(listOf(3, 0, 3), result)
    }

    @Test
    fun `the end goal`() {
        val result = changeMaker.calculate(listOf(1, 5, 10, 20), 43)

        assertEquals(result, listOf(3, 0, 0, 2))
    }

    @Test
    fun `different currency`() {
        val result = changeMaker.calculate(listOf(1, 2, 5, 10, 20, 50), 167)

        assertEquals(result, listOf(0, 1, 1, 1, 0, 3))
    }

    @Test
    fun `daniele problem`() {
        val result = changeMaker.calculate(listOf(1, 11, 25), 33)

        assertEquals(result, listOf(8, 0, 1))
    }
}

class ChangeMaker {
    fun calculate(denominations: List<Int>, changeAmount: Int): List<Int> {
        val denominationsInOrder = denominations.reversed()

        var changeLeft = changeAmount

        val coinsToReturn = denominationsInOrder.associateWith { 0 }.toMutableMap()

        denominationsInOrder.forEach { denomination ->
            while (changeLeft >= denomination) {
                coinsToReturn[denomination] = coinsToReturn[denomination]!! + 1
                changeLeft -= denomination
            }
        }

        return coinsToReturn.values.toList().reversed()
    }
}
