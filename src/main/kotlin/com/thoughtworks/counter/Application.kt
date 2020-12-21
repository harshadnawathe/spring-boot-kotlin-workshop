package com.thoughtworks.counter

import kotlin.random.Random


fun main() {
    val secret = Random.nextInt(1, 101)

    var count = 0
    var isDone = false
    while (count < 10 && !isDone) {
        println("Guess the number!")
        val guess: Int? = readLine()?.toIntOrNull()

        if (guess != null) {
            when {
                secret < guess -> println("guess is too large!")
                secret > guess -> println("guess is too small!")
                else -> {
                    println("You guessed it!!!!!")
                    isDone = true
                }
            }
        } else {
            println("Enter a valid number")
        }
        count++
        if(count == 10) {
            println("Game over.")
        }
    }
}
