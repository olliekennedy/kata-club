import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep

fun main() {
    sleep(5000)
    runBlocking {
        val windows = async(Dispatchers.IO) { order(Item.WINDOWS) }
        val doors = async(Dispatchers.IO) { order(Item.DOORS) }
        launch(Dispatchers.Default) {
            perform("lay bricks")
            launch { perform("install ${windows.await().description}") }
            launch { perform("install ${doors.await().description}") }
        }
    }
}

enum class Item(val description: String, val deliveryTime: Long) {
    WINDOWS("windows", 4000),
    DOORS("doors", 500),
}

private suspend fun order(item: Item): Item {
    println("ORDERED   -> ${item.description}")
    delay(item.deliveryTime)
    println("DELIVERED -> $item")
    return item
}

private suspend fun perform(task: String) {
    println("START:  $task")
    delay(1000)
    println("FINISH: $task")
}

//ORDERED   -> windows
//ORDERED   -> doors
//START:  lay bricks
//FINISH: lay bricks
//DELIVERED -> DOORS
//START:  install doors
//FINISH: install doors
//DELIVERED -> WINDOWS
//START:  install windows
//FINISH: install windows

//ORDERED   -> windows
//ORDERED   -> doors
//START:  lay bricks
//FINISH: lay bricks
//DELIVERED -> DOORS
//DELIVERED -> WINDOWS
//START:  install windows
//FINISH: install windows
//START:  install doors
//FINISH: install doors
