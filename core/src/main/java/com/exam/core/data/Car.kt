package com.exam.core.data

data class Car(
    var id: Long,
    val customerPrice: Int,
    val make: String,
    val marketPrice: Int,
    val model: String,
    val prosList: List<String>,
    val consList: List<String>,
    val rating: Int
)