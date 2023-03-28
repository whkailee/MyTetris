package com.example.mytetris.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mytetris.domain.BlockIndex
import com.example.mytetris.domain.TetrisBlock
import com.example.mytetris.domain.plus


fun DrawScope.drawTetrisBlock(
    tetrisBlock: TetrisBlock,
    blockSize: Float
) {
    for (blockIndex in tetrisBlock.shape) {
        drawBlock(
            coordinate = blockIndex + tetrisBlock.position,
            blockSize = blockSize,
            color = tetrisBlock.color
        )
    }
}

fun DrawScope.drawBlock(
    coordinate: BlockIndex,
    blockSize: Float,
    color: Color,
    alpha: Float = 1F,
    stroke: Dp = 1.dp
) {
    val actualLocation = Offset(
        coordinate.x * blockSize,
        coordinate.y * blockSize
    )
    val borderWidth = blockSize / 8
    drawTriangle(
        Color.White.copy(alpha),
        actualLocation,
        actualLocation + Offset(blockSize, 0F),
        actualLocation + Offset(0F, blockSize)
    )
    drawTriangle(
        Color.Black.copy(alpha),
        actualLocation + Offset(blockSize, 0F),
        actualLocation + Offset(blockSize, blockSize),
        actualLocation + Offset(0F, blockSize)
    )
    drawRect(
        color = color.copy(alpha),
        size = Size(
            blockSize - 2 * borderWidth,
            blockSize - 2 * borderWidth
        ),
        topLeft = actualLocation + Offset(borderWidth, borderWidth)
    )
    drawRect(
        color = Color(0xFF242A2F).copy(alpha),
        size = Size(blockSize, blockSize),
        style = Stroke(width = stroke.toPx()),
        topLeft = actualLocation
    )
}

fun DrawScope.drawTriangle(color: Color, point1: Offset, point2: Offset, point3: Offset) {
    val path = Path().apply {
        moveTo(point1.x, point1.y)
        lineTo(point2.x, point2.y)
        lineTo(point3.x, point3.y)
        close()
    }
    drawPath(path = path, color = color)
}

@Preview
@Composable
fun PreviewDrawBlock() {
    Canvas(modifier = Modifier.size(200.dp)) {
        drawTetrisBlock(
            tetrisBlock = TetrisBlock(
                shape = listOf(BlockIndex(0, 0), BlockIndex(1, 0), BlockIndex(1, 1), BlockIndex(1, 2)),
                position = BlockIndex(0, 0),
                color = Color.Yellow
            ),
            blockSize = 50.dp.toPx()
        )
    }
}