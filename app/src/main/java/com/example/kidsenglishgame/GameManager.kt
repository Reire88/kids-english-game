\
package com.example.kidsenglishgame

class GameManager {

    private val allItems = ItemBank.items.shuffled()
    private var roundItems: List<GameItem> = emptyList()
    private var index = 0

    var score: Int = 0
        private set

    // One retry allowed per question if the first attempt is wrong OR no result
    private var retryAvailable = true

    fun startNewRound() {
        score = 0
        index = 0
        retryAvailable = true
        // pick 10 unique items each round
        roundItems = allItems.shuffled().take(10)
    }

    fun currentItem(): GameItem? = roundItems.getOrNull(index)

    fun questionNumber(): Int = index + 1

    fun markCorrect() {
        score += 1
        advance()
    }

    fun markWrong() {
        if (retryAvailable) {
            // keep same question, consume retry
            retryAvailable = false
        } else {
            advance()
        }
    }

    fun markNeedsRetry() {
        // No speech recognized -> allow retry if still available
        // do not consume retry here; user just tries again
    }

    fun canRetryCurrent(): Boolean = retryAvailable

    fun isFinished(): Boolean = index >= 10

    private fun advance() {
        index += 1
        retryAvailable = true
    }

    fun prizeForScore(score: Int): GameItem {
        return ItemBank.prizes[score.coerceIn(0, 10)] ?: ItemBank.prizes[0]!!
    }
}
