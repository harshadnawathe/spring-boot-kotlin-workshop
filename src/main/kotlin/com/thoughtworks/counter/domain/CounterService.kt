package com.thoughtworks.counter.domain

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class CounterService(
    private val counterRepository: CounterRepository
) {

    fun createNew(name: String) = counterRepository.save(Counter(name))

    fun find(id: String): Mono<Counter> {
        return counterRepository.findById(id)
            .switchIfEmpty {
                Mono.error(CounterNotFoundException(id))
            }
    }

    fun increment(id: String): Mono<Counter> {
        return counterRepository.findById(id)
            .doOnNext {
                it.increment()
            }
            .flatMap {
                counterRepository.save(it)
            }.switchIfEmpty {
                Mono.error(CounterNotFoundException(id))
            }
    }

    fun decrement(id: String): Mono<Counter> {
        return counterRepository.findById(id)
            .doOnNext {
                it.decrement()
            }
            .flatMap {
                counterRepository.save(it)
            }.switchIfEmpty {
                Mono.error(CounterNotFoundException(id))
            }
    }
}

class CounterNotFoundException(id: String) : IllegalArgumentException("Counter with id $id not found")