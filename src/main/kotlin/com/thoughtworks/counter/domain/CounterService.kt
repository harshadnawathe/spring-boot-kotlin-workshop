package com.thoughtworks.counter.domain

import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class CounterService(
    private val counterRepository: CounterRepository
) {

    fun createNew(name: String) = counterRepository.save(Counter(name))

    fun find(id: String) : Counter {
        return counterRepository.findById(id)
            .orElseThrow {
                CounterNotFoundException(id)
            }
    }

    fun increment(id: String): Counter {
        return counterRepository.findById(id)
            .map {
                it.increment()
                counterRepository.save(it)
            }.orElseThrow{
                CounterNotFoundException(id)
            }
    }

    fun decrement(id: String): Counter {
        return counterRepository.findById(id)
            .map {
                it.decrement()
                counterRepository.save(it)
            }.orElseThrow {
                CounterNotFoundException(id)
            }
    }
}

class CounterNotFoundException(id: String) : IllegalArgumentException("Counter with id $id not found")