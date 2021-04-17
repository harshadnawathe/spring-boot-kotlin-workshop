package com.thoughtworks.counter


fun main() {
    println("Enter your name")
    val name: String = readLine() ?: ""
    val excitement = "!".repeat(name.length)
    println("Hello $name$excitement")
}