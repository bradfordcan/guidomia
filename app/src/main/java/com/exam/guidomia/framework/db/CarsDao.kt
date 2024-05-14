package com.exam.guidomia.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for [CarEntity]
 */
@Dao
interface CarsDao {

    /**
     * Inserts [carEntity] into the db and updates it if already exists
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCar(carEntity: CarEntity)

    /**
     * Deletes specified [carEntity] in the db
     */
    @Delete
    suspend fun deleteCar(carEntity: CarEntity)

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getCar(id: Long): CarEntity?

    @Query("SELECT * FROM cars")
    suspend fun getAllCars(): List<CarEntity>

}