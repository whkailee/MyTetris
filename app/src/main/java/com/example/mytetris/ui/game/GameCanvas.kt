package com.example.mytetris.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mytetris.domain.BlockIndex
import com.example.mytetris.domain.TetrisBlock
import kotlin.math.min

@Composable
fun GameCanvas(
    modifier: Modifier = Modifier,
    tetrisBlockInFlight: TetrisBlock,
    maxIndex: BlockIndex
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .border(2.dp, Color.White)
    ) {
        val blockSize = min(size.width / maxIndex.x, size.height / maxIndex.y)
        drawTetrisBlock(tetrisBlockInFlight, blockSize)
    }
}