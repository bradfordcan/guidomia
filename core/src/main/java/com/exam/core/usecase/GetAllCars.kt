package com.exam.core.usecase

import com.exam.core.repository.CarRepository

class GetAllCars(private val repository: CarRepository) {
    suspend operator fun invoke() = repository.getAllCars()
}