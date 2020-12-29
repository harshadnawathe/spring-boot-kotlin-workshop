package com.thoughtworks.counter.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CounterRepository : ReactiveMongoRepository<Counter, String> {

    fun findAllByName(name: String): Flux<Counter>
}