package com.thoughtworks.counter


fun main() {
    println("Enter your name")
    val name: String? = readLine()
    if (null == name) {
        println("Sorry, I didn't catch that.")
        return
    }
    println("Hello, $name")
}