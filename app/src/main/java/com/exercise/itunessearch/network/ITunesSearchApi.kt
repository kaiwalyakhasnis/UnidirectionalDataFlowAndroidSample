package com.exercise.itunessearch.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {

    @GET("/search")
    fun getPreviews(@Query("term") artist: String): Call<PreviewList>
}