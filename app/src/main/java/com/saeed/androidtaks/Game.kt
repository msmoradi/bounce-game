package com.saeed.androidtaks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.random.Random

const val objectSize = 30

@Composable
fun Game() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp

    val widthInPx = with(density) { widthInDp.roundToPx() }
    val heightInPx = with(density) { heightInDp.roundToPx() }

    var gameObjects by remember {
        mutableStateOf(
            generateInitialObjects(
                heightInPx,
                widthInPx
            )
        )
    }

    LaunchedEffect(true) {
        while (true) {
            gameObjects = gameObjects.map { gameObject ->
                val newPosition = gameObject.position + gameObject.velocity
                val newVelocity = handleCollision(
                    newPosition,
                    gameObject.velocity,
                    heightInPx,
                    widthInPx
                )

                gameObject.copy(
                    position = newPosition,
                    velocity = newVelocity
                )
            }

            delay(16) // Update at around 60 FPS
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        gameObjects.forEach { gameObject ->
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        translationX = gameObject.position.x.toFloat(),
                        translationY = gameObject.position.y.toFloat()
                    )
                    .size(objectSize.dp)
                    .background(getColorForType(gameObject.type))
            )
        }
    }
}

fun generateInitialObjects(screenHeight: Int, screenWidth: Int): List<GameObject> {
    return (0 until 15).map {
        val type = when (it) {
            in 0 until 5 -> GameObjectType.ROCK
            in 5 until 10 -> GameObjectType.PAPER
            else -> GameObjectType.SCISSORS
        }
        GameObject(
            type = type,
            position = IntOffset(
                Random.nextInt(0, screenWidth - objectSize * 4),
                Random.nextInt(0, screenHeight - objectSize * 4)
            ),
            velocity = IntOffset(Random.nextInt(-5, 5), Random.nextInt(-5, 5))
        )
    }
}

fun getColorForType(type: GameObjectType): Color {
    return when (type) {
        GameObjectType.ROCK -> Color.Red
        GameObjectType.PAPER -> Color.Green
        GameObjectType.SCISSORS -> Color.Blue
    }
}

fun handleCollision(
    position: IntOffset,
    velocity: IntOffset,
    screenHeight: Int,
    screenWidth: Int
): IntOffset {

    // Handle collision with walls
    return handleCollisionWithWalls(
        position = position,
        velocity = velocity,
        screenHeight = screenHeight,
        screenWidth = screenWidth
    )

    // TODO Handle collision with other game objects
}

fun handleCollisionWithWalls(
    position: IntOffset,
    velocity: IntOffset,
    screenHeight: Int,
    screenWidth: Int
): IntOffset {
    var newVelocity = velocity

    if (position.x <= 0 || position.x >= screenWidth - objectSize * 3) {
        newVelocity = IntOffset(-velocity.x, velocity.y)
    }
    if (position.y <= 0 || position.y >= screenHeight - objectSize * 6) {
        newVelocity = IntOffset(velocity.x, -velocity.y)
    }
    return newVelocity
}

@Preview
@Composable
fun GamePreview() {
    Game()
}