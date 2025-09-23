import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RomanNumeralsTest {
    val underTest = RomanNumeralGenerator()

    @Test
    fun `one becomes I`() {
        val result = underTest.convertTo(1)

        assertEquals("I", result)
    }

    @Test
    fun `two becomes II`() {
        val result = underTest.convertTo(2)

        assertEquals("II", result)
    }

    @Test
    fun `three becomes III`() {
        val result = underTest.convertTo(3)

        assertEquals("III", result)
    }

    @Test
    fun `four becomes IV`() {
        val result = underTest.convertTo(4)

        assertEquals("IV", result)
    }

    @Test
    fun `wha tthe hell is 1499`() {
        val result = underTest.convertTo(1499)

        assertEquals("MCDXCIX", result)
    }

    @Test
    fun `the rest`() {
        assertEquals("V", underTest.convertTo(5))
        assertEquals("VI", underTest.convertTo(6))
        assertEquals("IX", underTest.convertTo(9))
        assertEquals("X", underTest.convertTo(10))
        assertEquals("XL", underTest.convertTo(40))
        assertEquals("LX", underTest.convertTo(60))
        assertEquals("C", underTest.convertTo(100))
        assertEquals("XC", underTest.convertTo(90))
        assertEquals("D", underTest.convertTo(500))
        assertEquals("CD", underTest.convertTo(400))
        assertEquals("M", underTest.convertTo(1000))
        assertEquals("CM", underTest.convertTo(900))
        assertEquals("MMCMXCIX", underTest.convertTo(2999))
        assertEquals("MMM", underTest.convertTo(3000))
    }
}

class RomanNumeralGenerator {

    val numerals = listOf(
        1000 to "M",
        900 to "CM",
        500 to "D",
        400 to "CD",
        100 to "C",
        90 to "XC",
        50 to "L",
        40 to "XL",
        10 to "X",
        9 to "IX",
        5 to "V",
        4 to "IV",
        1 to "I",
    )

    fun convertTo(i: Int): String =
        numerals.fold(i to "") { acc, numeralPair ->
            val (number, numeral) = numeralPair
            val (howManyLeft, output) = acc
            howManyLeft % number to output + numeral.repeat(howManyLeft / number)
        }.second
}
