package com.thoughtworks.counter


fun main() {
    print("Enter you name: ")
    val userName: String? = readLine()

    println("Hello, $userName${"!".repeat(excitementLevel(userName))}")
}

fun excitementLevel(name: String?) = name?.length ?: 0