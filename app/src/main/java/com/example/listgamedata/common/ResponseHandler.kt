package com.example.listgamedata.common

sealed class ResponseHandler<T> (
    val value : T? = null,
    val msg : String? = null
) {

    class Success<T>(value: T?) : ResponseHandler<T>(value)

    class Fail<T>(msg: String, value: T?=null) : ResponseHandler<T>(value, msg)

    class Loading<T> : ResponseHandler<T>()

}