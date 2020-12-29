package com.thoughtworks.counter.http

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [
    CounterController::class
])
class CounterControllerWebMvcTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var mockCounterService: CounterService

    @BeforeEach
    fun setUp() {
        `when`(mockCounterService.createNew("my-counter")).thenReturn(
            Counter("my-counter", 0, "some-counter-id")
        )

        `when`(mockCounterService.find("some-counter-id")).thenReturn(
            Counter("my-counter", 0, "some-counter-id")
        )

        `when`(mockCounterService.increment("some-counter-id")).thenReturn(
            Counter("my-counter", 1, "some-counter-id")
        )
    }

    @Test
    fun `should handle request to create new Counter`() {

        mockMvc.post("/counter-service/counter") {
            contentType = APPLICATION_JSON
            content = "{ \"name\": \"my-counter\"}"
        }.andExpect {
            status {
                is2xxSuccessful
            }
            content {
                json("{\"name\": \"my-counter\", \"count\": 0}")
            }
        }
    }

    @Test
    fun `should handle request to find a Counter by id`() {
        mockMvc.get("/counter-service/counter/some-counter-id")
            .andExpect {
                status { is2xxSuccessful }
                content {
                    json("{\"name\": \"my-counter\", \"count\": 0}")
                }
            }
    }

    @Test
    fun `should handle request to increment a Counter by id`() {
        mockMvc.post("/counter-service/counter/some-counter-id/increment")
            .andExpect {
                status { is2xxSuccessful }
                content {
                    json("{\"name\": \"my-counter\", \"count\": 1}")
                }
            }
    }
}