package com.exam.guidomia.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CarsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCar(carEntity: CarEntity)

    @Delete
    suspend fun deleteCar(carEntity: CarEntity)

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getCar(id: Long): CarEntity?

    @Query("SELECT * FROM cars")
    suspend fun getAllCars(): List<CarEntity>

}