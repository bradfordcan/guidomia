package com.exam.core.usecase

import com.exam.core.repository.CarRepository

class GetCar(private val repository: CarRepository) {
    suspend operator fun invoke(id: Long) = repository.getCar(id)
}