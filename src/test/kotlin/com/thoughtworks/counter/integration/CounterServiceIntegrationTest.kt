package com.thoughtworks.counter.integration

import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus


internal data class TestCounterResponse(
    val name: String,
    val count: Int,
    val id: String
) {
    fun jsonText() = "{\"name\": \"$name\", \"count\" : $count, \"id\": \"$id\"}"
}

@SpringBootTest(webEnvironment = RANDOM_PORT)
class CounterServiceIntegrationTest {

    @Autowired
    lateinit var template: TestRestTemplate

    @Test
    fun `end to end Counter test`() {

        val randomCounterName = RandomStringUtils.randomAlphabetic(5)
        val counterId = createNewCounterAndCheck(randomCounterName)
        incrementAndCheck(counterId, randomCounterName)
        decrementAndCheck(counterId, randomCounterName)
        getAndCheck(counterId, randomCounterName)
    }

    private fun getAndCheck(counterId: String, randomCounterName: String) {
        val getCounterResponse = template.getForEntity(
            "/counter-service/counter/$counterId",
            TestCounterResponse::class.java
        )
        assertThat(getCounterResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getCounterResponse.body).isEqualTo(
            TestCounterResponse(randomCounterName, 0, counterId)
        )
    }

    private fun decrementAndCheck(counterId: String, randomCounterName: String) {
        val decrementResponse = template.postForEntity(
            "/counter-service/counter/$counterId/decrement",
            null,
            TestCounterResponse::class.java
        )
        assertThat(decrementResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(decrementResponse.body).isEqualTo(
            TestCounterResponse(randomCounterName, 0, counterId)
        )
    }

    private fun incrementAndCheck(counterId: String, randomCounterName: String) {
        val incrementResponse = template.postForEntity(
            "/counter-service/counter/$counterId/increment",
            null,
            String::class.java
        )
        assertThat(incrementResponse.statusCode).isEqualTo(HttpStatus.OK)
        JSONAssert.assertEquals(
            TestCounterResponse(randomCounterName, 1, counterId).jsonText(),
            incrementResponse.body,
            true
        )
    }

    private fun createNewCounterAndCheck(randomCounterName: String?): String {
        val createCounterRequestBody = mapOf("name" to randomCounterName)

        val createCounterResponse = template.postForEntity(
            "/counter-service/counter",
            createCounterRequestBody,
            TestCounterResponse::class.java
        )

        assertThat(createCounterResponse.statusCode).isEqualTo(HttpStatus.OK)
        val createCounterResponseBody = createCounterResponse.body!!
        assertThat(createCounterResponseBody.name).isEqualTo(randomCounterName)
        assertThat(createCounterResponseBody.count).isEqualTo(0)

        val counterId = createCounterResponseBody.id
        return counterId
    }
}