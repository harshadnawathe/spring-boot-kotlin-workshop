package com.thoughtworks.counter.http

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterNotFoundException
import com.thoughtworks.counter.domain.CounterService
import com.thoughtworks.counter.domain.CounterUnderflowError
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.kotlin.core.publisher.toMono

@WebFluxTest(
    controllers = [
        CounterController::class
    ]
)
class CounterControllerWebFluxTest {

    @Autowired
    lateinit var client: WebTestClient

    @MockBean
    lateinit var mockCounterService: CounterService

    @BeforeEach
    fun setUp() {
        `when`(mockCounterService.createNew("my-counter")).thenReturn(
            Counter("my-counter", 0, "some-counter-id").toMono()
        )

        `when`(mockCounterService.find("some-counter-id")).thenReturn(
            Counter("my-counter", 0, "some-counter-id").toMono()
        )

        `when`(mockCounterService.find("invalid-id")).thenReturn(
            CounterNotFoundException("invalid-id").toMono()
        )

        `when`(mockCounterService.increment("some-counter-id")).thenReturn(
            Counter("my-counter", 1, "some-counter-id").toMono()
        )

        `when`(mockCounterService.decrement("some-counter-id")).thenReturn(
            Counter("my-counter", 0, "some-counter-id").toMono()
        )

        `when`(mockCounterService.decrement("zero-count")).thenReturn(
            CounterUnderflowError("zero-count").toMono()
        )
    }

    @Test
    fun `should handle request to create new Counter`() {

        client.post()
            .uri("/counter-service/counter")
            .contentType(APPLICATION_JSON)
            .bodyValue("{ \"name\": \"my-counter\"}")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .json("{\"name\": \"my-counter\", \"count\": 0}")
    }

    @Test
    fun `should handle request to find a Counter by id`() {

        client.get()
            .uri("/counter-service/counter/some-counter-id")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .json("{\"name\": \"my-counter\", \"count\": 0}")

    }

    @Test
    fun `should handle request to increment a Counter by id`() {
        client.post()
            .uri("/counter-service/counter/some-counter-id/increment")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .json("{\"name\": \"my-counter\", \"count\": 1}")
    }

    @Test
    fun `should handle request to decrement a Counter by id`() {
        client.post()
            .uri("/counter-service/counter/some-counter-id/decrement")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .json("{\"name\": \"my-counter\", \"count\": 0}")
    }


    @Test
    fun `should response with status 404 when Counter is not present`() {

        client.get()
            .uri("/counter-service/counter/invalid-id")
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .json("{\"message\": \"Counter with id invalid-id not found\"}")
    }

    @Test
    fun `should response with status 409 when Counter cannot be decremented`() {
        client.post()
            .uri("/counter-service/counter/zero-count/decrement")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody()
            .json("{\"message\": \"Cannot decrement counter zero-count as count is zero\"}")
    }
}