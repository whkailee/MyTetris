package com.example.mytetris.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mytetris.domain.BlockIndex
import com.example.mytetris.domain.TetrisBlock

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameUi(
    currentScore: Int,
    bestScore: Int,
    tetrisBlockInFlight: TetrisBlock?,
    tetrisBlockLanded: TetrisBlock?,
    maxIndex: BlockIndex,
    moveCount: Int
) {
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
                tetrisBlockInFlight = tetrisBlockInFlight,
                tetrisBlockLanded = tetrisBlockLanded,
                maxIndex = maxIndex
            )
        }
    }
}