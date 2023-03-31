package com.example.mytetris.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytetris.domain.*
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

    val maxIndex by mutableStateOf(BlockIndex(MAX_WIDTH, MAX_HEIGHT))
    var tetrisBlockInFlight by mutableStateOf<TetrisBlock?>(null)
    var tetrisBlockLanded by mutableStateOf<TetrisBlock?>(null)

    private var _moveCount = MutableStateFlow(0)
    val moveCount = _moveCount.asStateFlow()
    private var clockJob: Job? = null

    fun resumeGame() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (this.coroutineContext.isActive) {
                delay(1000)
                updatePosition()
            }
        }
    }

    fun pauseGame() {
        clockJob?.cancel()
    }

    fun onTap() {
        tetrisBlockInFlight?.rotate()
    }

    fun onMove(moveDirection: MoveDirection) =
         when(moveDirection) {
            MoveDown -> {
                while (canMoveDown()) {
                    tetrisBlockInFlight!!.position.y++
                }
                true
            }
            is MoveHorizontally -> {
                if (tetrisBlockInFlight != null && canMoveHorizontally(moveDirection.numberOfBlocks)) {
                    tetrisBlockInFlight!!.position.x += moveDirection.numberOfBlocks
                    true
                } else {
                    false
                }
            }
        }

    private fun updatePosition() {
        println("updating...")

        if (tetrisBlockInFlight == null) {
            tetrisBlockInFlight = TetrisBlock.getRandomTetrisBlock(width = MAX_WIDTH)
            return
        }

        tetrisBlockInFlight?.let { tetrisBlockInFlight ->
            if (canMoveDown()) {
                tetrisBlockInFlight.position.y++
            } else {
                landingTetrisBlock()
            }
        }

        _moveCount.value++
    }

    private fun canMoveDown(): Boolean {
        tetrisBlockInFlight?.let { tetrisBlockInFlight ->
            for (blockIndex in tetrisBlockInFlight.shape) {
                if (blockIndex.y + tetrisBlockInFlight.position.y + 1 >= MAX_HEIGHT) {
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
        }?: return false
        return true
    }

    private fun canMoveHorizontally(num: Int): Boolean {
        tetrisBlockInFlight?.let { tetrisBlockInFlight ->
            for (blockIndex in tetrisBlockInFlight.shape) {
                if (blockIndex.x + tetrisBlockInFlight.position.x + num >= MAX_WIDTH || blockIndex.x + tetrisBlockInFlight.position.x + num < 0) {
                    return false
                }
                tetrisBlockLanded?.let { tetrisBlockLanded ->
                    for (landed in tetrisBlockLanded.shape) {
                        if (blockIndex.x + tetrisBlockInFlight.position.x + num == landed.x + tetrisBlockLanded.position.x &&
                            blockIndex.y + tetrisBlockInFlight.position.y == landed.y + tetrisBlockLanded.position.y
                        ) {
                            return false
                        }
                    }
                }
            }
        }

        return true
    }

    private fun landingTetrisBlock() {
        tetrisBlockInFlight?.let { tetrisBlockInFlight ->
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

        checkElimination()
    }

    private fun checkElimination() {
        tetrisBlockLanded?.apply {
            var eliminationFound = true
            while (eliminationFound) {
                eliminationFound = false
                shape.groupBy({ it.y }, { it.x })
                    .filter {
                        it.value.size == MAX_WIDTH
                    }.firstNotNullOfOrNull {
                        eliminationFound = true
                        it.key
                    }?.let { eliminatedLineY ->
                        shape = shape.filter {
                            it.y != eliminatedLineY
                        }.map {
                            if (it.y < eliminatedLineY) {
                                it.y++
                            }
                            it
                        }
                    }
            }
        }
    }

    companion object {
        private const val MAX_WIDTH = 10
        private const val MAX_HEIGHT = 20
    }
}

class GameViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return GameViewModel(context = context) as T
    }
}