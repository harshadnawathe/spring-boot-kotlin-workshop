package com.thoughtworks.counter


fun main() {
    println("Enter your name")
    val name: String = readLine() ?: ""
    println("Hello $name${"!".repeat(name.length)}")
}