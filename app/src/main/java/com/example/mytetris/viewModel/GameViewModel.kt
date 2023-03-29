package com.example.mytetris.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytetris.domain.BlockIndex
import com.example.mytetris.domain.TetrisBlock
import com.example.mytetris.domain.getRealPositionShapeList
import com.example.mytetris.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(context: Context) : ViewModel() {
    private val repository = GameRepository(context)

    val currentScore by mutableStateOf(repository.currentScore)
    val bestScore by mutableStateOf(repository.bestScore)

    val maxIndex by mutableStateOf(BlockIndex(10, 20))
    var tetrisBlockInFlight by mutableStateOf<TetrisBlock?>(null)
    var tetrisBlockLanded by mutableStateOf<TetrisBlock?>(null)

    private var _moveCount = MutableStateFlow(0)
    val moveCount = _moveCount.asStateFlow()
    private var clockJob: Job? = null

    fun resumeGame() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (this.coroutineContext.isActive) {
                delay(300)
                updatePosition()
            }
        }
    }

    fun pauseGame() {
        clockJob?.cancel()
    }

    private fun updatePosition() {
        println("updating...")

        if (tetrisBlockInFlight == null) {
            tetrisBlockInFlight = TetrisBlock.getRandomTetrisBlock(width = 10)
            return
        }

        tetrisBlockInFlight?.let { tetrisBlockInFlight ->
            if (canMoveDown()) {
                tetrisBlockInFlight.position.y++
            } else {
                if (tetrisBlockLanded == null) {
                    tetrisBlockLanded = TetrisBlock(
                        tetrisBlockInFlight.getRealPositionShapeList(),
                        BlockIndex(0, 0),
                        Color.Gray
                    )
                } else {
                    tetrisBlockLanded?.let { tetrisBlockLanded ->
                        tetrisBlockLanded.shape += tetrisBlockInFlight.getRealPositionShapeList()
                    }
                }
                this.tetrisBlockInFlight = null
            }
        }

        _moveCount.value++
    }

    private fun canMoveDown(): Boolean {
        tetrisBlockInFlight?.let { tetrisBlockInFlight ->
            for (blockIndex in tetrisBlockInFlight.shape) {
                if (blockIndex.y + tetrisBlockInFlight.position.y >= 19) {
                    return false
                }
                tetrisBlockLanded?.let { tetrisBlockLanded ->
                    for (landed in tetrisBlockLanded.shape) {
                        if (blockIndex.x + tetrisBlockInFlight.position.x == landed.x + tetrisBlockLanded.position.x &&
                            blockIndex.y + tetrisBlockInFlight.position.y + 1 == landed.y + tetrisBlockLanded.position.y
                        ) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }


}

class GameViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return GameViewModel(context = context) as T
    }
}