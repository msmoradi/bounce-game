package com.saeed.androidtaks

import androidx.compose.ui.unit.IntOffset

data class GameObject(
    val type: GameObjectType,
    var position: IntOffset,
    var velocity: IntOffset
)