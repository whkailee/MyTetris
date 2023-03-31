package com.example.mytetris.domain

sealed class MoveDirection
class MoveHorizontally(val numberOfBlocks: Int): MoveDirection()
object MoveDown: MoveDirection()