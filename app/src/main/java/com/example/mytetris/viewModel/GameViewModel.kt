package com.example.mytetris.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytetris.domain.BlockIndex
import com.example.mytetris.domain.TetrisBlock
import com.example.mytetris.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(context: Context) : ViewModel() {
    private val repository = GameRepository(context)

    val currentScore by mutableStateOf(repository.currentScore)
    val bestScore by mutableStateOf(repository.bestScore)

    val maxIndex by mutableStateOf(BlockIndex(10, 20))
    var tetrisBlockInFlight by mutableStateOf(TetrisBlock.getRandomTetrisBlock(maxIndex.x))
    private val clock = MutableStateFlow(Any())

    init {
        viewModelScope.launch {
            while(this.coroutineContext.isActive) {
                delay(1000)
                clock.emit(Any())
            }
        }

        viewModelScope.launch {
            clock.collectLatest {
                update()
            }
        }
    }

    private fun update() {
        println("updating...")
        tetrisBlockInFlight = TetrisBlock(
            shape = tetrisBlockInFlight.shape,
            position = BlockIndex(tetrisBlockInFlight.position.x, tetrisBlockInFlight.position.y + 1),
            color = tetrisBlockInFlight.color
        )
    }

}

class GameViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return GameViewModel(context = context) as T
    }
}