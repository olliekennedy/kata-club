import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class MarsRoverTest {

    @Test
    fun `create a rover with a starting position and direction`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.SOUTH)

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(Direction.SOUTH))
    }

    @Test
    fun `move a rover one move forward`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.SOUTH)

        rover.moveForward()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 2)))
        assertThat(rover.direction, equalTo(Direction.SOUTH))
    }

    @Test
    fun `move a rover one move backward`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.SOUTH)

        rover.moveBackward()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 0)))
        assertThat(rover.direction, equalTo(Direction.SOUTH))
    }
}

enum class Direction {
    SOUTH
}

class Rover(startingPosition: Coordinate, startingDirection: Direction) {
    fun moveForward() {
        position = position.copy(y = position.y + 1)
    }

    fun moveBackward() {
        position = position.copy(y = position.y - 1)
    }

    var position: Coordinate = startingPosition
    val direction: Direction = startingDirection
}

data class Coordinate(val x: Int, val y: Int)
