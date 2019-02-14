package com.exercise.itunessearch.common
import android.arch.lifecycle.MutableLiveData

sealed class State {
    object LOADING : State()
    object SUCCESS : State()
    object ERROR : State()
}

data class Data<T>(
        val State: State,
        val data: T? = null,
        val message: String? = null
)

fun <T> MutableLiveData<Data<T>>.setSuccess(data: T? = null) {
    value = Data(State.SUCCESS, data)
}

fun <T> MutableLiveData<Data<T>>.setLoading() {
    value = Data(State.LOADING, value?.data)
}

fun <T> MutableLiveData<Data<T>>.setError(message: String? = null) {
    value = Data(State.ERROR, value?.data, message)
}
