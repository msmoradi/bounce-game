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
                heightInPx / 2,
                widthInPx / 2
            )
        )
    }

    LaunchedEffect(true) {
        while (true) {
            gameObjects = gameObjects.map { gameObject ->
                val newPosition = gameObject.position + gameObject.velocity
                // TODO handle collision

                gameObject.copy(
                    position = newPosition,
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
    return List(5) {
        GameObject(
            type = GameObjectType.ROCK,
            IntOffset(Random.nextInt(0, screenHeight), Random.nextInt(0, screenWidth)),
            IntOffset(Random.nextInt(-5, 5), Random.nextInt(-5, 5))
        )

    } + List(5) {
        GameObject(
            type = GameObjectType.PAPER,
            IntOffset(Random.nextInt(0, screenHeight), Random.nextInt(0, screenWidth)),
            IntOffset(Random.nextInt(-5, 5), Random.nextInt(-5, 5))
        )
    } + List(5) {
        GameObject(
            type = GameObjectType.SCISSORS,
            IntOffset(Random.nextInt(0, screenHeight), Random.nextInt(0, screenWidth)),
            IntOffset(Random.nextInt(-5, 5), Random.nextInt(-5, 5))
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

@Preview
@Composable
fun GamePreview() {
    Game()
}