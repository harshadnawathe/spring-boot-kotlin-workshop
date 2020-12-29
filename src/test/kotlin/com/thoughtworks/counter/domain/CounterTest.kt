package com.thoughtworks.counter.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CounterTest {

    @Test
    fun `should construct a counter with given name`() {
        val counter = Counter("my-counter")

        val name = counter.name

        assertThat(name).isEqualTo("my-counter")
    }

    @Test
    fun `should create a counter with default count`() {
        val counter = Counter("my-counter")

        val current: Int = counter.count

        assertThat(current).isEqualTo(0)
    }

    @Test
    fun `should increment the count by 1`() {
        val counter = Counter("my-counter")

        counter.increment()

        assertThat(counter.count).isEqualTo(1)
    }

    @Test
    fun `should decrement the count by 1`() {
        val counter = Counter("my-counter").apply {
            increment()
        }

        counter.decrement()

        assertThat(counter.count).isEqualTo(0)
    }

    @Test
    fun `should throw exception on decrement when current count is 0`() {
        val counter = Counter("my-counter", 0, "some-id")

        assertThatThrownBy {
            counter.decrement()
        }.hasMessage("Cannot decrement counter some-id as count is zero")
    }

    @Test
    fun `should throw CounterUnderflowError on decrement when current count is 0`() {
        val counter = Counter("my-counter", 0, "some-id")

        assertThatThrownBy {
            counter.decrement()
        }.isInstanceOf(CounterUnderflowError::class.java)
    }
}