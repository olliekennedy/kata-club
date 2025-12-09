import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

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

    @ParameterizedTest
    @MethodSource("rotateLeft")
    fun `rotate a rover left`(starting: Direction, result: Direction) {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = starting)

        rover.rotateLeft()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(result))
    }

    @ParameterizedTest
    @MethodSource("rotateRight")
    fun `rotate a rover right`(starting: Direction, result: Direction) {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = starting)

        rover.rotateRight()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(result))
    }

    @Test
    fun `rotate a rover right`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.SOUTH)

        rover.rotateRight()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(Direction.WEST))
    }

    @Test
    fun `move forward EAST`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.EAST)

        rover.moveForward()

        assertThat(rover.position, equalTo(Coordinate(x = 1, y = 1)))
        assertThat(rover.direction, equalTo(Direction.EAST))
    }

    @Test
    fun `move forward WEST`() {
        val rover = Rover(startingPosition = Coordinate(x = 1, y = 0), startingDirection = Direction.WEST)

        rover.moveForward()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 0)))
        assertThat(rover.direction, equalTo(Direction.WEST))
    }

    @Test
    fun `move forward NORTH`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 1), startingDirection = Direction.NORTH)

        rover.moveForward()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 0)))
        assertThat(rover.direction, equalTo(Direction.NORTH))
    }

    @Test
    fun `move backward facing EAST`() {
        val rover = Rover(startingPosition = Coordinate(x = 1, y = 0), startingDirection = Direction.EAST)

        rover.moveBackward()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 0)))
        assertThat(rover.direction, equalTo(Direction.EAST))
    }

    @Test
    fun `move backward facing WEST`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 0), startingDirection = Direction.WEST)

        rover.moveBackward()

        assertThat(rover.position, equalTo(Coordinate(x = 1, y = 0)))
        assertThat(rover.direction, equalTo(Direction.WEST))
    }

    @Test
    fun `move backward facing NORTH`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 0), startingDirection = Direction.NORTH)

        rover.moveBackward()

        assertThat(rover.position, equalTo(Coordinate(x = 0, y = 1)))
        assertThat(rover.direction, equalTo(Direction.NORTH))
    }

    @Test
    fun `go on a journey`() {
        val rover = Rover(startingPosition = Coordinate(x = 0, y = 0), startingDirection = Direction.SOUTH)

        rover.moveForward()
        rover.moveForward()
        rover.rotateLeft()
        rover.moveForward()
        rover.moveForward()
        rover.rotateRight()

        assertThat(rover.position, equalTo(Coordinate(x = 2, y = 2)))
        assertThat(rover.direction, equalTo(Direction.SOUTH))
    }

    @Test
    fun `show a current map`() {
        val rover = Rover(startingPosition = Coordinate(x = 3, y = 5), startingDirection = Direction.EAST)

        val result = rover.showMap()

        val expected = """          
          
          
  >       
          
          
          
          
          
          """
        assertThat(result, equalTo(expected))
    }

    companion object {
        @JvmStatic
        fun rotateLeft(): List<Arguments?> {
            return listOf(
                arguments(Direction.NORTH, Direction.WEST),
                arguments(Direction.WEST, Direction.SOUTH),
                arguments(Direction.SOUTH, Direction.EAST),
                arguments(Direction.EAST, Direction.NORTH),
            )
        }

        @JvmStatic
        fun rotateRight(): List<Arguments?> {
            return listOf(
                arguments(Direction.NORTH, Direction.EAST),
                arguments(Direction.EAST, Direction.SOUTH),
                arguments(Direction.SOUTH, Direction.WEST),
                arguments(Direction.WEST, Direction.NORTH),
            )
        }
    }
}

enum class Direction {
    SOUTH,
    EAST,
    WEST,
    NORTH,
}

class Rover(startingPosition: Coordinate, startingDirection: Direction) {
    fun moveForward() {
        position = when (direction) {
            Direction.SOUTH -> position.copy(y = position.y + 1)
            Direction.NORTH -> position.copy(y = position.y - 1)
            Direction.EAST -> position.copy(x = position.x + 1)
            Direction.WEST -> position.copy(x = position.x - 1)
        }
    }

    fun moveBackward() {
        position = when (direction) {
            Direction.SOUTH -> position.copy(y = position.y - 1)
            Direction.NORTH -> position.copy(y = position.y + 1)
            Direction.EAST -> position.copy(x = position.x - 1)
            Direction.WEST -> position.copy(x = position.x + 1)
        }
    }

    fun rotateLeft() {
        direction = when (direction) {
            Direction.SOUTH -> Direction.EAST
            Direction.EAST -> Direction.NORTH
            Direction.NORTH -> Direction.WEST
            Direction.WEST -> Direction.SOUTH
        }
    }

    fun rotateRight() {
        direction = when (direction) {
            Direction.SOUTH -> Direction.WEST
            Direction.EAST -> Direction.SOUTH
            Direction.NORTH -> Direction.EAST
            Direction.WEST -> Direction.NORTH
        }
    }

    fun showMap(): String {
        val twoDeeMap = mutableListOf(
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
            mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
        )

        var output = ""

        for (j in 0..9) {
            for (i in 0..9) {
                if (i == position.x && j == position.y) {
                    output = output + ">"
                } else {
                    output = output + " "
                }
            }
            output = output + "\n"
        }
        return output
    }

    var position: Coordinate = startingPosition
    var direction: Direction = startingDirection
}

data class Coordinate(val x: Int, val y: Int)
