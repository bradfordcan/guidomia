package com.exam.guidomia.framework.di

import com.exam.core.repository.CarRepository
import com.exam.core.usecase.AddCar
import com.exam.core.usecase.DeleteCar
import com.exam.core.usecase.GetAllCars
import com.exam.core.usecase.GetCar
import com.exam.guidomia.framework.UseCases
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {
    @Provides
    fun getUseCases(repository: CarRepository) = UseCases(
        AddCar(repository),
        GetAllCars(repository),
        GetCar(repository),
        DeleteCar(repository)
    )
}