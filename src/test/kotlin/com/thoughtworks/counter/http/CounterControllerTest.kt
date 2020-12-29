package com.thoughtworks.counter.http

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterService
import io.mockk.every
import io.mockk.mockk
import org.apache.commons.lang3.RandomStringUtils.random
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CounterControllerTest {

    private val counterService = mockk<CounterService>() {
        every { createNew(any()) } answers { Counter(arg(0), 0, random(5)) }
        every { find(any()) } answers { Counter("my-counter", 5, arg(0)) }
        every { increment(any()) } answers { Counter("my-counter", 6, arg(0)) }
        every { decrement(any()) } answers { Counter("my-counter", 4, arg(0)) }
    }

    @Test
    fun `should create a Counter and return response with given name`() {
        val controller = CounterController(counterService)
        val request = CreateCounterRequest(
            name = "my-counter"
        )

        val response: CounterResponse = controller.create(request)

        assertThat(response.name).isEqualTo("my-counter")
    }

    @Test
    fun `should create a Counter and return response with count`() {
        val controller = CounterController(counterService)
        val request = CreateCounterRequest(
            name = "my-counter"
        )

        val response: CounterResponse = controller.create(request)

        assertThat(response.count).isEqualTo(0)
    }

    @Test
    fun `should find a Counter with given id`() {
        val controller = CounterController(counterService)

        val response: CounterResponse = controller.find(id = "my-counter")

        assertThat(response.id).isEqualTo("my-counter")
    }

    @Test
    fun `should increment a Counter with given id`() {
        val controller = CounterController(counterService)

        val response: CounterResponse = controller.increment(id = "my-counter")

        assertThat(response.count).isEqualTo(6)
    }

    @Test
    fun `should decrement a Counter with given id`() {
        val controller = CounterController(counterService)

        val response: CounterResponse = controller.decrement(id = "my-counter")

        assertThat(response.count).isEqualTo(4)
    }
}