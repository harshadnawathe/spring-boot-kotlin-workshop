package com.thoughtworks.counter.http

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterNotFoundException
import com.thoughtworks.counter.domain.CounterService
import io.mockk.every
import io.mockk.mockk
import org.apache.commons.lang3.RandomStringUtils.random
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

class CounterControllerTest {

    private val counterService = mockk<CounterService>() {
        every { createNew("my-counter") } answers { Counter("my-counter", 0, random(5)).toMono() }
        every { find("my-counter") } answers { Counter("my-counter", 5, "my-counter").toMono() }
        every { find("invalid-id") } returns Mono.error(CounterNotFoundException("invalid-id"))
        every { increment("my-counter") } answers { Counter("my-counter", 6, "my-counter").toMono() }
        every { decrement("my-counter") } answers { Counter("my-counter", 4, "my-counter").toMono() }
    }

    @Test
    fun `should create a Counter and return response with given name`() {
        val controller = CounterController(counterService)
        val request = CreateCounterRequest(
            name = "my-counter"
        )

        val response: CounterResponse = controller.create(request).block()!!

        assertThat(response.name).isEqualTo("my-counter")
    }

    @Test
    fun `should create a Counter and return response with count`() {
        val controller = CounterController(counterService)
        val request = CreateCounterRequest(
            name = "my-counter"
        )

        val response: CounterResponse = controller.create(request).block()!!

        assertThat(response.count).isEqualTo(0)
    }

    @Test
    fun `should find a Counter with given id`() {
        val controller = CounterController(counterService)

        val response: CounterResponse = controller.find(id = "my-counter").block()!!

        assertThat(response.id).isEqualTo("my-counter")
    }

    @Test
    fun `should increment a Counter with given id`() {
        val controller = CounterController(counterService)

        val response: CounterResponse = controller.increment(id = "my-counter").block()!!

        assertThat(response.count).isEqualTo(6)
    }

    @Test
    fun `should decrement a Counter with given id`() {
        val controller = CounterController(counterService)

        val response: CounterResponse = controller.decrement(id = "my-counter").block()!!

        assertThat(response.count).isEqualTo(4)
    }

    @Test
    fun `should throw CounterNotFoundException when id is invalid`() {
        val controller = CounterController(counterService)

        assertThrows<CounterNotFoundException> {
            controller.find(id = "invalid-id").block()
        }
    }
}