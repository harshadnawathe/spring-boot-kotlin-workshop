package com.thoughtworks.counter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "counter")
class Counter(
    @Indexed
    val name: String
) {

    @PersistenceConstructor
    constructor(name: String, count: Int, id: String) : this(name) {
        this.count = count
        this.id = id
    }

    @Id
    lateinit var id: String
        private set

    fun increment() {
        count++
    }

    fun decrement() {
        check(count > 0) {
            "Can not decrement when count is 0"
        }

        count--
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Counter

        if (name != other.name) return false
        if (count != other.count) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + count
        return result
    }

    var count = 0
        private set


}