package com.example.mytetris.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.mytetris.domain.BlockIndex
import com.example.mytetris.domain.TetrisBlock
import kotlin.math.min

@Composable
fun GameCanvas(
    modifier: Modifier = Modifier,
    tetrisBlockInFlight: TetrisBlock?,
    tetrisBlockLanded: TetrisBlock?,
    maxIndex: BlockIndex
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .border(2.dp, Color.White)
    ) {
        val blockSize = min(size.width / maxIndex.x, size.height / maxIndex.y)
        val diffX = size.width - blockSize * maxIndex.x
        val diffY = size.height - blockSize * maxIndex.y

        val topLeft = Offset(diffX / 2, diffY / 2)
        drawRect(
            color = Color.Black,
            topLeft = topLeft,
            size = Size(blockSize * maxIndex.x, blockSize * maxIndex.y),
            style = Fill
        )

        if (tetrisBlockInFlight != null) {
            drawTetrisBlock(topLeft, tetrisBlockInFlight, blockSize)
        }
        if (tetrisBlockLanded != null) {
            drawTetrisBlock(topLeft, tetrisBlockLanded, blockSize)
        }
    }
}