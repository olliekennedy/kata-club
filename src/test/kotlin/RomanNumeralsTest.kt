import org.junit.jupiter.api.Nested
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

    @Nested
    inner class ArabicToRoman {
        @ParameterizedTest
        @ArgumentsSource(RomanNumeralsCsvProvider::class)
        fun `number to numeral`(numeral: String, number: Int) {
            assertEquals(numeral, RomanNumeralGenerator().arabicToRomanWithFold(number))
        }

        @ParameterizedTest
        @ArgumentsSource(RomanNumeralsCsvProvider::class)
        fun `number to numeral using replacement`(numeral: String, number: Int) {
            assertEquals(numeral, RomanNumeralGenerator().arabicToRomanWithReplace(number))
        }

        @ParameterizedTest
        @ArgumentsSource(RomanNumeralsCsvProvider::class)
        fun `number to numeral using replacement and fold`(numeral: String, number: Int) {
            assertEquals(numeral, RomanNumeralGenerator().arabicToRomanWithReplaceAndFold(number))
        }
    }

    @Nested
    inner class RomanToArabic {
        @ParameterizedTest
        @ArgumentsSource(RomanNumeralsCsvProvider::class)
        fun `numeral to number`(numeral: String, number: Int) {
            assertEquals(number, RomanNumeralGenerator().romanToArabic(numeral))
        }

        @ParameterizedTest
        @ArgumentsSource(RomanNumeralsCsvProvider::class)
        fun `numeral to number using recursion`(numeral: String, number: Int) {
            assertEquals(number, RomanNumeralGenerator().romanToArabicRecursively(numeral))
        }
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

    fun arabicToRomanWithReplace(i: Int): String =
        "I".repeat(i)
            .replace("I".repeat(1000), "M")
            .replace("I".repeat(900), "CM")
            .replace("I".repeat(500), "D")
            .replace("I".repeat(400), "CD")
            .replace("I".repeat(100), "C")
            .replace("I".repeat(90), "XC")
            .replace("I".repeat(50), "L")
            .replace("I".repeat(40), "XL")
            .replace("I".repeat(10), "X")
            .replace("I".repeat(9), "IX")
            .replace("I".repeat(5), "V")
            .replace("I".repeat(4), "IV")
            .replace("I".repeat(1), "I")

    fun arabicToRomanWithReplaceAndFold(i: Int): String {
        return "I".repeat(i).let { output ->
            numerals.fold(output) { acc, pair ->
                acc.replace("I".repeat(pair.first), pair.second)
            }
        }
    }

    fun arabicToRomanWithFold(i: Int): String =
        numerals.fold(i to "") { acc, numeralPair ->
            val (number, numeral) = numeralPair
            val (howManyLeft, output) = acc
            howManyLeft % number to output + numeral.repeat(howManyLeft / number)
        }.second

    fun romanToArabic(string: String): Int =
        numerals.fold(0 to string) { acc, numeralPair ->
            val (number, numeral) = numeralPair
            var (runningTotal, inputNumeral) = acc
            while (inputNumeral.startsWith(numeral)) {
                runningTotal += number
                inputNumeral = inputNumeral.removeRange(0, numeral.length)
            }
            runningTotal to inputNumeral
        }.first

    fun romanToArabicRecursively(initialRoman: String): Int {
        tailrec fun checkTheRest(input: String, acc: Int): Int {
            for ((number, numeral) in numerals) {
                if (input.startsWith(numeral)) {
                    return checkTheRest(input.removePrefix(numeral), acc + number)
                }
            }
            return acc
        }
        return checkTheRest(initialRoman, 0)
    }
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
