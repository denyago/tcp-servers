package name.denyago.tcpsrvz.core

import kotlin.random.Random

class GameState {
    private val guessed by lazy { mutableMapOf<Int, String>() }
    val numbers: Set<Int> by lazy {
        (1..100).map { Random.nextInt(Game.MAX_NUM) }.toSet()
    }

    fun checkGuess(guess: Int) =
        numbers.contains(guess)

    fun findWhoGuessed(guess: Int): String? =
        guessed.get(guess)

    fun saveWhoGuessed(guess: Int, userName: String) =
        guessed.set(guess, userName)
}
