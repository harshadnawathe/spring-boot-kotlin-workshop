package com.thoughtworks.counter

import kotlin.random.Random

enum class Comparison {
    Large,
    EqualTo,
    Small
}

class Secret: Comparable<Int> {
    private val number: Int = Random.nextInt(1, 101)

    override fun compareTo(other: Int): Int {
        return number.compareTo(other)
    }

    fun comparisonWith(guess: Int) : Comparison {
        val comparison = compareTo(guess)
        return when {
            comparison < 0 -> Comparison.Large
            comparison > 0 -> Comparison.Small
            else -> Comparison.EqualTo
        }
    }
}

enum class GameState {
    InPlay,
    GameOver,
    PlayerWon
}

class Game {
    private val secret = Secret()

    private var attempts = 0
    var state = GameState.InPlay
        private set

    fun check(guess: Int) {
        when(secret.comparisonWith(guess)) {
            Comparison.Large -> println("guess is too large!")
            Comparison.Small -> println("guess is too small!")
            Comparison.EqualTo -> {
                println("You guessed it!!!!!")
                state = GameState.PlayerWon
            }
        }

        attempts++
        if(attempts >= MAX_ATTEMPTS) {
            state = GameState.GameOver
            println("Game over.")
        }
    }

    companion object {
        private const val MAX_ATTEMPTS = 10
    }
}


fun main() {

    val game = Game()
    while (game.state == GameState.InPlay) {
        println("Guess the number!")
        val guess: Int? = readLine()?.toIntOrNull()

        if (guess != null) {
            game.check(guess)
        } else {
            println("Enter a valid number")
        }
    }
}
