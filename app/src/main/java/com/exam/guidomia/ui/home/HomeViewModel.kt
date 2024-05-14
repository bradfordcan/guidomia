package com.exam.guidomia.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.core.data.Car
import com.exam.guidomia.framework.UseCases
import com.exam.guidomia.framework.di.ApplicationModule
import com.exam.guidomia.framework.di.DaggerViewModelComponent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    @Inject
    lateinit var useCases: UseCases

    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build()
            .inject(this)
    }

    val cars = MutableLiveData<List<Car>>()

    fun getAllCars() {
        coroutineScope.launch {
            // val carsList = useCases.getAllCars()
            val carsList = getJsonDataFromAsset("car_list.json")
            if(carsList != null) {
                cars.postValue(jsonToCar(carsList))
            }
        }
    }

    private fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun jsonToCar(jsonString: String): List<Car> {
        val listPersonType = object : TypeToken<List<Car>>() {}.type
        return Gson().fromJson(jsonString, listPersonType)
    }

}