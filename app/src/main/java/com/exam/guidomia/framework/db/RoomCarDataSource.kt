package com.exam.guidomia.framework.db

import android.content.Context
import com.exam.core.data.Car
import com.exam.core.repository.CarDataSource

class RoomCarDataSource(context: Context): CarDataSource {

    private val carsDao = DatabaseService.getInstance(context).carsDao()
    override suspend fun add(car: Car) = carsDao.addCar(CarEntity.fromCar(car))

    override suspend fun get(id: Long): Car? = carsDao.getCar(id)?.toCar()

    override suspend fun getAll(): List<Car> = carsDao.getAllCars().map { it.toCar() }

    override suspend fun delete(car: Car) = carsDao.deleteCar(CarEntity.fromCar(car))

}