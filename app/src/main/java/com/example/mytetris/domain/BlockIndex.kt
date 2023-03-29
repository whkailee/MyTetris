package com.example.mytetris.domain

class BlockIndex(var x: Int, var y: Int)

operator fun BlockIndex.plus(index: BlockIndex): BlockIndex {
    return BlockIndex(x + index.x, y + index.y)
}