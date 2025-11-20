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

    @Test
    fun `rotate a rover left`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.SOUTH)

        rover.rotateLeft()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(Direction.EAST))
    }

    @Test
    fun `rotate a rover right`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.SOUTH)

        rover.rotateRight()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(Direction.WEST))
    }
}

enum class Direction {
    SOUTH,
    EAST,
    WEST,
}

class Rover(startingPosition: Coordinate, startingDirection: Direction) {
    fun moveForward() {
        when (direction) {
            Direction.SOUTH -> position = position.copy(y = position.y + 1)
            else -> TODO()
        }
    }

    fun moveBackward() {
        when (direction) {
            Direction.SOUTH -> position = position.copy(y = position.y - 1)
            else -> TODO()
        }
    }

    fun rotateLeft() {
        when (direction) {
            Direction.SOUTH -> direction = Direction.EAST
            else -> TODO()
        }
    }

    fun rotateRight() {
        when (direction) {
            Direction.SOUTH -> direction = Direction.WEST
            else -> TODO()
        }
    }

    var position: Coordinate = startingPosition
    var direction: Direction = startingDirection
}

data class Coordinate(val x: Int, val y: Int)
