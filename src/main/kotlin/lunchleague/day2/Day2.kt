package org.example.lunchleague.day2

fun main() {
    val whatIWant = puzzleInput.split(",").map { it.split("-").map { it.toLong() }.let { (it[0]..it[1]).toList().map { it.toString() } } }

//    println(whatIWant)

    val invalidIds = mutableListOf<Long>()

    whatIWant.forEach { range: List<String> ->
        range.forEach { number: String ->
            if (number.isInvalid()) {
                invalidIds.add(number.toLong())
            }
        }
    }

    println(invalidIds.sum())
}

fun String.isInvalid(): Boolean {
    val factors = mutableListOf<Int>()

    for (i in 1..this.length) {
        if (this.length % i == 0) {
            if (i != this.length) factors.add(i)
        }
    }



    if (this.first() == '0') {
//        println("Invalid $this")
        return true
    }

//    println("Factors: $factors")
    for (factor in factors) {
//        println("Checking factor: $factor for $this")
//        if (factor)
        val parts = this.chunked(factor)

//        println("Parts: $parts")

        if (parts.all { it == parts[0] }) {
//            println("Factor Invalid $this")
            return true
        }
    }

    return false
}

val input = """11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"""
val firstThing = """95-115"""
val puzzleInput = """92916254-92945956,5454498003-5454580069,28-45,4615-7998,4747396917-4747534264,272993-389376,36290651-36423050,177-310,3246326-3418616,48-93,894714-949755,952007-1003147,3-16,632-1029,420-581,585519115-585673174,1041-1698,27443-39304,71589003-71823870,97-142,2790995-2837912,579556301-579617006,653443-674678,1515120817-1515176202,13504-20701,1896-3566,8359-13220,51924-98061,505196-638209,67070129-67263432,694648-751703,8892865662-8892912125"""