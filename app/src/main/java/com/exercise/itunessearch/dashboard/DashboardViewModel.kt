package com.exercise.itunessearch.dashboard

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.SparseBooleanArray
import com.exercise.itunessearch.common.*
import com.exercise.itunessearch.data.DataRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

class DashboardViewModel(private val dataRepository: DataRepository, val player:ExoPlayer) : ViewModel() {

    val previewMusic = SingleLiveEvent<Pair<Int,String?>>()
    val previewList = MutableLiveData<Data<List<PreviewListModel?>?>>()
    val playPauseState = SparseBooleanArray()

    fun retrievePreviewList(term: String) {
        previewList.setLoading()
        dataRepository
                .getPreviewList(term, {
                    previewList.setSuccess(it)
                }, {
                    previewList.setError(it)
                })
    }
}

class DashboardViewModelFactory(private val dataRepository: DataRepository,private val exoPlayer:ExoPlayer) :
        ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(dataRepository,exoPlayer) as T
    }
}