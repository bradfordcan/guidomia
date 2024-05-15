package com.exam.guidomia.framework.di

import com.exam.guidomia.ui.home.HomeViewModel
import dagger.Component

@Component(modules = [ApplicationModule::class, RepositoryModule::class, UseCasesModule::class, SharedPreferencesModule::class])
interface ViewModelComponent {
    fun inject(homeViewModel: HomeViewModel)
}