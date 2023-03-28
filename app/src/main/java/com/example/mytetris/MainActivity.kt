package com.example.mytetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.mytetris.ui.game.GameUi
import com.example.mytetris.ui.theme.MyTetrisTheme
import com.example.mytetris.viewModel.GameViewModel
import com.example.mytetris.viewModel.GameViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<GameViewModel> {
        GameViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTetrisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameUi(
                        currentScore = viewModel.currentScore,
                        bestScore = viewModel.bestScore,
                        tetrisBlockInFlight = viewModel.tetrisBlockInFlight,
                        maxIndex = viewModel.maxIndex
                    )
                }
            }
        }
    }
}