import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class CheckoutTest {
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
                argumentSet("buy an item on offer multiple times", listOf("A", "A", "A", "A", "A", "A", "A"), 310),
                argumentSet("buy another item on offer", listOf("B", "B"), 50),
                argumentSet("buy two items on offer", listOf("A", "A", "A", "A", "B", "B"), 230),
                argumentSet("order doesn't matter", listOf("A", "B", "A", "A", "B", "A", "C", "B").shuffled(), 280),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("expectations")
    fun `it costs nothing to buy nothing`(items: List<String>, expected: Int) {
        val basket = Basket()

        items.forEach { basket.add(it) }

        assertEquals(expected, basket.total())
    }
}

class Basket {
    val items = mutableMapOf<String, Int>()

    data class Price(
        val normal: Int,
        val offer: Offer? = null,
    )

    data class Offer(
        val price: Int,
        val quantity: Int,
    )

    val catalogue = mapOf(
        "A" to Price(50, Offer(130, 3)),
        "B" to Price(30, Offer(50, 2)),
        "C" to Price(20),
    )

    fun total(): Int =
        items.entries.sumOf { (item, count) ->
            catalogue[item]?.let { price ->
                price.offer?.let { offer ->
                    val howManyOffers = count / offer.quantity
                    val countLeft = count % offer.quantity
                    (offer.price * howManyOffers) + (price.normal * countLeft)
                } ?: (price.normal * count)
            } ?: 0
        }

    fun add(sku: String) {
        items[sku] = (items[sku] ?: 0) + 1
    }
}
