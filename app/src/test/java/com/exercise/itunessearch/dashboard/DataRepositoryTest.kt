package com.exercise.itunessearch.dashboard

import com.exercise.itunessearch.data.DataRepository
import com.exercise.itunessearch.network.ITunesSearchApi
import com.exercise.itunessearch.network.PreviewItem
import com.exercise.itunessearch.network.PreviewList
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepositoryTest {

    @Mock
    lateinit var iTunesSearchApi: ITunesSearchApi

    @Mock
    lateinit var getPreviewListCall: Call<PreviewList>

    lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        dataRepository = DataRepository(iTunesSearchApi)
    }

    @Test
    fun `test getPreviewList success`() {

        whenever(iTunesSearchApi
                .getPreviews("AKON"))
                .thenReturn(getPreviewListCall)

        whenever(getPreviewListCall
                .enqueue(any()))
                .thenAnswer {
                    (it.arguments[0] as? Callback<PreviewList>)
                            ?.onResponse(getPreviewListCall, Response.success(getFakePreviewList()))
                }

        dataRepository.getPreviewList("AKON", {
            assertTrue(it?.size==2)
            assertEquals((it?.get(0) as PreviewItem).artworkUrl100,"akon")
            assertEquals((it.get(1) as PreviewItem).artworkUrl100,"passenger")
        }, {})
    }

    fun getFakePreviewList() = PreviewList(10, listOf(PreviewItem("akon"),
            PreviewItem("passenger")))
}