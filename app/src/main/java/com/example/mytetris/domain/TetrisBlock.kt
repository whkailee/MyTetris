package com.example.mytetris.domain

import androidx.compose.ui.graphics.Color

class TetrisBlock(var shape: List<BlockIndex>, var position: BlockIndex, var color: Color) {

    companion object {

        fun getRandomTetrisBlock(width: Int): TetrisBlock {
            return shapeVariants.random().let {
                TetrisBlock(
                    shape = it.first,
                    position = BlockIndex((1 until width - 1).random(), 1),
                    color = it.second
                )
            }
        }

        private val shapeVariants = listOf(
            listOf(
                BlockIndex(0, -1), BlockIndex(0, 0), BlockIndex(-1, 0), BlockIndex(-1, 1)
            ) to Color(0xFF3D76B5), listOf(
                BlockIndex(0, -1), BlockIndex(0, 0), BlockIndex(1, 0), BlockIndex(1, 1)
            ) to Color(0xFFA369B8), listOf(
                BlockIndex(0, -1), BlockIndex(0, 0), BlockIndex(0, 1), BlockIndex(0, 2)
            ) to Color(0xFFFF0128), listOf(
                BlockIndex(0, 1), BlockIndex(0, 0), BlockIndex(0, -1), BlockIndex(1, 0)
            ) to Color(0xFF43D462), listOf(
                BlockIndex(0, 0), BlockIndex(-1, 0), BlockIndex(0, -1), BlockIndex(-1, -1)
            ) to Color(0xFFFBCD05), listOf(
                BlockIndex(-1, -1), BlockIndex(0, -1), BlockIndex(0, 0), BlockIndex(0, 1)
            ) to Color(0xFF53B1FD), listOf(
                BlockIndex(1, -1), BlockIndex(0, -1), BlockIndex(0, 0), BlockIndex(0, 1)
            ) to Color(0xFFEDEAE9)
        )
    }
}

fun TetrisBlock.getRealPositionShapeList(): List<BlockIndex> {
    return shape.map {
        BlockIndex(it.x + position.x, it.y + position.y)
    }
}
