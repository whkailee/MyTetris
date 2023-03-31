package com.example.mytetris.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.mytetris.domain.*
import kotlin.math.abs

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameUi(
    currentScore: Int,
    bestScore: Int,
    tetrisBlockInFlight: TetrisBlock?,
    tetrisBlockLanded: TetrisBlock?,
    maxIndex: BlockIndex,
    moveCount: Int,
    onTap: () -> Unit,
    onMove: (MoveDirection) -> Boolean
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Tetris") },
                modifier = Modifier.fillMaxWidth(),
                contentColor = Color.White,
                backgroundColor = MaterialTheme.colors.primaryVariant,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Add, contentDescription = "New Game")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                contentColor = Color.White,
                backgroundColor = MaterialTheme.colors.primaryVariant
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Current Score: $currentScore")
                    Text(text = "Best Score: $bestScore")
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            GameCanvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                if (abs(offsetX) < abs(offsetY) && offsetY > 0) {
                                    onMove(MoveDown)
                                }
                                offsetX = 0f
                                offsetY = 0f
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                            val numberOfBlocks = (offsetX / (size.width / maxIndex.x)).toInt()
                            if (abs(offsetX) > abs(offsetY) && numberOfBlocks != 0) {
                                if (onMove(MoveHorizontally(numberOfBlocks))) {
                                    println("detectDragGestures onDrag move succeeded")
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures {
                            println("detectTapGestures onTap")
                            onTap()
                        }
                    },
                tetrisBlockInFlight = tetrisBlockInFlight,
                tetrisBlockLanded = tetrisBlockLanded,
                maxIndex = maxIndex
            )
        }
    }
}