package com.exercise.itunessearch.dashboard

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.exercise.itunessearch.common.State
import com.exercise.itunessearch.data.DataRepository
import com.google.android.exoplayer2.ExoPlayer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DashboardViewModelTest {

    @Mock
    lateinit var dataRepository: DataRepository

    @Mock
    lateinit var exoPlayer: ExoPlayer

    lateinit var dashboardViewModel: DashboardViewModel

    @Rule
    fun rule() = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        dashboardViewModel = DashboardViewModel(dataRepository, exoPlayer)
    }

    @Test
    fun `test retrievePreviewList loading`() {
        dashboardViewModel.retrievePreviewList("AKON")
        //loading
        assertTrue(dashboardViewModel.previewList.value?.State == State.LOADING)
    }

    @Test
    fun `test retrievePreviewList success`() {
        whenever(dataRepository
                .getPreviewList(any(), any(), any()))
                .thenAnswer {
                    (it.arguments[1] as ((List<PreviewListModel?>?) -> Unit)?)
                            ?.invoke(emptyList())
                }

        dashboardViewModel.retrievePreviewList("AKON")

        assertTrue(dashboardViewModel.previewList.value?.State == State.SUCCESS)
        assertTrue(dashboardViewModel.previewList.value?.data?.isEmpty()?:false)
    }

    @Test
    fun `test retrievePreviewList error`() {
        whenever(dataRepository
                .getPreviewList(any(), any(), any()))
                .thenAnswer {
                    (it.arguments[2] as (((String) -> Unit)?))
                            ?.invoke("unable to load list")
                }

        dashboardViewModel.retrievePreviewList("AKON")

        assertTrue(dashboardViewModel.previewList.value?.State == State.ERROR)
        assertTrue(dashboardViewModel.previewList.value?.message?.contentEquals("unable to load list")?:false)
    }
}