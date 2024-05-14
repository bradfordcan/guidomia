package com.exam.guidomia.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CarEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class DatabaseService : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "cars.db"

        private var instance: DatabaseService? = null

        private fun createDatabase(context: Context): DatabaseService =
            Room.databaseBuilder(context, DatabaseService::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration().build()

        fun getInstance(context: Context): DatabaseService =
            (instance ?: createDatabase(context)).also { instance = it }
    }

    abstract fun carsDao(): CarsDao

}