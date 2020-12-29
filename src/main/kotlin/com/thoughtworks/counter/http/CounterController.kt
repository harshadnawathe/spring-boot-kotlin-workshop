package com.thoughtworks.counter.http

import com.thoughtworks.counter.domain.Counter
import com.thoughtworks.counter.domain.CounterNotFoundException
import com.thoughtworks.counter.domain.CounterService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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
    fun create(@RequestBody request: CreateCounterRequest): CounterResponse {
        return CounterResponse(
            counter = counterService.createNew(request.name)
        )
    }

    @GetMapping(
        path = ["/{counter-id}"]
    )
    fun find(@PathVariable("counter-id") id: String): CounterResponse {
        return CounterResponse(
            counter = counterService.find(id)
        )
    }

    @PostMapping(
        path = ["/{counter-id}/increment"]
    )
    fun increment(@PathVariable("counter-id") id: String): CounterResponse {
        return CounterResponse(
            counter = counterService.increment(id)
        )
    }

    @PostMapping(
        path = ["/{counter-id}/decrement"]
    )
    fun decrement(@PathVariable("counter-id") id: String): CounterResponse {
        return CounterResponse(
            counter = counterService.decrement(id)
        )
    }
}