package com.thoughtworks.counter.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.commons.lang3.RandomStringUtils.random
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class CounterServiceTest {

    private val repository = mockk<CounterRepository> {
        every { findById(any()) } answers { Optional.of(Counter("my-counter", 5, arg(0))) }
        every { save(any<Counter>()) } answers { arg(0) }
    }

    @Test
    fun `should return a new counter with given name`() {
        val service = CounterService(repository)

        val counter = service.createNew(name = "my-counter")

        assertThat(counter.name).isEqualTo("my-counter")
    }

    @Test
    fun `should save the new counter in the repository`() {
        val service = CounterService(repository)

        service.createNew(name = "counter")

        verify {
            repository.save(match<Counter> {
                it.name == "counter"
            })
        }
    }

    @Test
    fun `should increment the counter with given id`() {
        val service = CounterService(repository)

        val counter = service.increment(id = "some-id")!!

        assertThat(counter.count).isEqualTo(6)
    }

    @Test
    fun `should save the incremented counter in the repository`() {
        val service = CounterService(repository)

        service.increment(id = "some-id")

        verify {
            repository.save(match<Counter> {
                it.id == "some-id" &&
                        it.count == 6
            })
        }
    }

    @Test
    fun `should return null when counter to increment is not present`() {
        every {
            repository.findById("non-existent-id")
        } returns Optional.empty()

        val service = CounterService(repository)

        val counter: Counter? = service.increment(id = "non-existent-id")

        assertThat(counter).isNull()
    }

    @Test
    fun `should save the new counter in the repository 1`() {
        lateinit var saved: Counter
        val fakeRepository = mockk<CounterRepository> {
            every { save(any<Counter>()) } answers {
                val arg: Counter = arg(0)
                Counter(name = arg.name, count = arg.count, id = random(5)).also {
                    saved = it
                }
            }
        }
        val service = CounterService(fakeRepository)

        val counter = service.createNew(name = "counter")

        assertThat(saved).isEqualTo(counter)
    }

    @Test
    fun `should decrement the counter with given id`() {
        val service = CounterService(repository)

        val counter = service.decrement(id = "some-id")!!

        assertThat(counter.count).isEqualTo(4)
    }

    @Test
    fun `should save the decremented counter in the repository`() {
        val service = CounterService(repository)

        service.decrement(id = "some-id")

        verify {
            repository.save(match<Counter> {
                it.id == "some-id" &&
                        it.count == 4
            })
        }
    }

    @Test
    fun `should return null when counter to decrement is not present`() {
        every {
            repository.findById("non-existent-id")
        } returns Optional.empty()

        val service = CounterService(repository)

        val counter: Counter? = service.decrement(id = "non-existent-id")

        assertThat(counter).isNull()
    }
}

