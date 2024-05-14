package com.exam.guidomia.framework

import com.exam.core.usecase.AddCar
import com.exam.core.usecase.DeleteCar
import com.exam.core.usecase.GetAllCars
import com.exam.core.usecase.GetCar

data class UseCases(
    val addCar: AddCar,
    val getAllCars: GetAllCars,
    val getCar: GetCar,
    val deleteCar: DeleteCar
)