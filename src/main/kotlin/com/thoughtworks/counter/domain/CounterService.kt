package com.thoughtworks.counter.domain

class CounterService(
    private val counterRepository: CounterRepository
) {

    fun createNew(name: String) = counterRepository.save(Counter(name))

    fun increment(id: String): Counter? {
        return counterRepository.findById(id)
            .map {
                it.increment()
                counterRepository.save(it)
            }.orElse(null)
    }

    fun decrement(id: String): Counter? {
        return counterRepository.findById(id)
            .map {
                it.decrement()
                counterRepository.save(it)
            }.orElse(null)
    }
}