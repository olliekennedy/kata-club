import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class MarsRoverTest {

    @Test
    fun `create a rover with a starting position and direction`() {
        val rover = Rover(startingPosition = Coordinate(0, 1), startingDirection = Direction.SOUTH)

        assertThat(rover.position, equalTo(Coordinate(0, 1)))
        assertThat(rover.direction, equalTo(Direction.SOUTH))
    }
}

enum class Direction {
    SOUTH
}

class Rover(startingPosition: Coordinate, startingDirection: Direction) {
    val position: Coordinate = startingPosition
    val direction: Direction = startingDirection
}

data class Coordinate(val x: Int, val y: Int)
