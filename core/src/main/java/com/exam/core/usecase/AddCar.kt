package com.exam.core.usecase

import com.exam.core.data.Car
import com.exam.core.repository.CarRepository

class AddCar(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car) = repository.addCar(car)
}