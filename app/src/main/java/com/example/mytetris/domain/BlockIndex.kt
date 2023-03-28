package com.example.mytetris.domain

class BlockIndex(val x: Int, val y: Int)

operator fun BlockIndex.plus(index: BlockIndex): BlockIndex {
    return BlockIndex(x + index.x, y + index.y)
}