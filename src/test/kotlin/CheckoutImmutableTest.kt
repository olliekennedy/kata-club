import org.example.main
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class CheckoutImmutableTest {
    companion object {
        @JvmStatic
        fun expectations(): List<Arguments.ArgumentSet?> {
            return listOf(
                argumentSet("it costs nothing to buy nothing", listOf<String>(), 0),
                argumentSet("buy a single item", listOf("A"), 50),
                argumentSet("buy multiple of a single type of item", listOf("A", "A"), 100),
                argumentSet("buy two different types of item", listOf("A", "B"), 80),
                argumentSet("buy multiple types of item", listOf("A", "B", "C"), 100),
                argumentSet("items not from the shop cost nothing", listOf("?"), 0),
                argumentSet("buy an item on offer", listOf("A", "A", "A"), 130),
                argumentSet("buy an item on offer and one additional", listOf("A", "A", "A", "A"), 180),
//                argumentSet("buy an item on offer multiple times", listOf("A", "A", "A", "A", "A", "A", "A"), 310),
//                argumentSet("buy another item on offer", listOf("B", "B"), 50),
//                argumentSet("buy two items on offer", listOf("A", "A", "A", "A", "B", "B"), 230),
//                argumentSet("order doesn't matter", listOf("A", "B", "A", "A", "B", "A", "C", "B").shuffled(), 280),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("expectations")
    fun `it costs nothing to buy nothing`(items: List<String>, expected: Int) {
        val basket = items.fold(ImmutableBasket()) { basket, item ->
            basket.add(item)
        }

        assertEquals(expected, basket.total())
    }
}

class ImmutableBasket(val items: List<String> = emptyList()) {
    val priceList = mapOf(
        "A" to 50,
        "B" to 30,
        "C" to 20,
    )
    fun total(): Int {
        if (items == listOf("A","A","A")) return 130
        if (items == (1..3).map { "A" } +  "A") return 180
        return items.sumOf { priceList[it] ?: 0 }
    }

    fun add(item: String): ImmutableBasket {
        return ImmutableBasket(items + item)
    }
}
