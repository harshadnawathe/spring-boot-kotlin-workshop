package com.thoughtworks.counter.domain

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CounterRepository : MongoRepository<Counter, String> {

    fun findAllByName(name: String) : List<Counter>
}