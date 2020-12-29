package com.thoughtworks.counter.db.mongo

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class CounterRepositoryIntegrationTest {

    @Autowired
    lateinit var repository: CounterRepository

    @Test
    fun `should store a counter in the repository`() {
        val saved = Counter("my-counter")
            .apply {
                increment()
                increment()
            }.let { counter ->
                repository.save(counter)
            }.block()!!

        val result = repository.findById(saved.id).block()!!

        assertThat(result).isEqualTo(saved)
    }

    @Test
    fun `should fetch a counter by name`() {
        val saved = Counter("someone-else's-counter")
            .apply {
                increment()
                increment()
            }.let { counter ->
                repository.save(counter)
            }.block()!!

        val result: List<Counter> = repository.findAllByName("someone-else's-counter").collectList().block()!!

        assertThat(result)
            .hasSize(1)
            .contains(saved)
    }
}