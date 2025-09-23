import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream
import kotlin.test.assertEquals

class RomanNumeralsTest {

    @ParameterizedTest
    @ArgumentsSource(RomanNumeralsCsvProvider::class)
    fun `number to numeral`(numeral: String, number: Int) {
        assertEquals(numeral, RomanNumeralGenerator().convertTo(number))
    }

    @ParameterizedTest
    @ArgumentsSource(RomanNumeralsCsvProvider::class)
    fun `numeral to number`(numeral: String, number: Int) {
        assertEquals(number, RomanNumeralGenerator().convertFrom(numeral))
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

    fun convertFrom(string: String): Int =
        numerals.fold(0 to string) { acc, numeralPair ->
            val (number, numeral) = numeralPair
            var (runningTotal, inputNumeral) = acc
            while (inputNumeral.startsWith(numeral)) {
                runningTotal += number
                inputNumeral = inputNumeral.removeRange(0, numeral.length)
            }
            runningTotal to inputNumeral
        }.first
}

class RomanNumeralsCsvProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val path = Paths.get("src/test/resources/roman_numerals.csv")
        return Files.lines(path)
            .filter { it.isNotBlank() }
            .map {
                val (numeral, number) = it.split(",")
                Arguments.of(numeral, number.toInt())
            }
    }
}
