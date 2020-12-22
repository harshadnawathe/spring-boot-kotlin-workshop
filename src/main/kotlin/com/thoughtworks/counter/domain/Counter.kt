package com.thoughtworks.counter.domain

class Counter(val name: String) {

    fun increment() {
       count++
    }

    fun decrement() {
        check(count > 0) {
            "Can not decrement when count is 0"
        }

        count--
    }

    var count = 0
        private set
}