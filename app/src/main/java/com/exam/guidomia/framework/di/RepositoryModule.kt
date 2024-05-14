package com.exam.guidomia.framework.di

import android.app.Application
import com.exam.core.repository.CarRepository
import com.exam.guidomia.framework.db.RoomCarDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(app: Application) = CarRepository(RoomCarDataSource(app))
}