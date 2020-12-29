package com.thoughtworks.counter.http

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterNotFoundException
import com.thoughtworks.counter.domain.CounterService
import com.thoughtworks.counter.domain.CounterUnderflowError
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


data class CreateCounterRequest(val name: String)

data class CounterResponse(
    val name: String,
    val count: Int,
    val id: String
) {
    constructor(counter: Counter) : this(
        name = counter.name,
        count = counter.count,
        id = counter.id
    )
}

@RestController
@RequestMapping(path = ["/counter-service/counter"])
class CounterController(
    private val counterService: CounterService
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@RequestBody request: CreateCounterRequest): Mono<CounterResponse> {
        return counterService.createNew(request.name)
            .map {
                CounterResponse(it)
            }

    }

    @GetMapping(
        path = ["/{counter-id}"]
    )
    fun find(@PathVariable("counter-id") id: String): Mono<CounterResponse> {
        return counterService.find(id)
            .map {
                CounterResponse(it)
            }
    }

    @PostMapping(
        path = ["/{counter-id}/increment"]
    )
    fun increment(@PathVariable("counter-id") id: String): Mono<CounterResponse> {
        return counterService.increment(id)
            .map {
                CounterResponse(it)
            }
    }

    @PostMapping(
        path = ["/{counter-id}/decrement"]
    )
    fun decrement(@PathVariable("counter-id") id: String): Mono<CounterResponse> {
        return counterService.decrement(id)
            .map {
                CounterResponse(it)
            }

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleException(e: CounterNotFoundException): Any {
        return mapOf("message" to e.message)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleException(e: CounterUnderflowError): Any {
        return mapOf("message" to e.message)
    }
}