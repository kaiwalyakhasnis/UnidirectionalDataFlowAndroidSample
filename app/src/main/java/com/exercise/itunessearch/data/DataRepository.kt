package com.exercise.itunessearch.data

import com.exercise.itunessearch.dashboard.PreviewListEmpty
import com.exercise.itunessearch.dashboard.PreviewListModel
import com.exercise.itunessearch.network.ITunesSearchApi
import com.exercise.itunessearch.network.PreviewList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository(private val iTunesSearchApi: ITunesSearchApi) {

    fun getPreviewList(term: String, success: ((List<PreviewListModel?>?) -> Unit)?, failure: ((String) -> Unit)?) {
        iTunesSearchApi
                .getPreviews(term)
                .enqueue(object : Callback<PreviewList> {
                    override fun onFailure(call: Call<PreviewList>, t: Throwable) {
                        t.message?.let { message -> failure?.invoke(message) }
                    }

                    override fun onResponse(call: Call<PreviewList>, response: Response<PreviewList>) {
                        if (response.isSuccessful) {
                            if (response.body()?.resultCount == 0) {
                                success?.invoke(listOf(PreviewListEmpty()))
                                return
                            } else {
                                response.body()?.results?.let { result ->
                                    success?.invoke(result)
                                    return
                                }
                            }
                        }
                        failure?.invoke("Something went wrong.. Try again")
                    }

                })
    }
}