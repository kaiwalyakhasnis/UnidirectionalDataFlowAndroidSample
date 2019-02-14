package com.exercise.itunessearch.dashboard

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.exercise.itunessearch.common.ActivityScope
import com.exercise.itunessearch.common.AppComponent
import com.exercise.itunessearch.data.DataRepository
import com.exercise.itunessearch.network.ITunesSearchApi
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ActivityScope
@Component(dependencies = [(AppComponent::class)],
        modules = [(DashboardModule::class), (ExoPlayerModule::class)])
interface DashboardComponent {
    fun inject(dashboardActivity: DashboardActivity)
}


@Module(includes = [(ExoPlayerModule::class)])
class DashboardModule(private val dashboardActivity: DashboardActivity) {

    @ActivityScope
    @Provides
    fun providesITunesApi(retrofit: Retrofit): ITunesSearchApi = retrofit
            .create(ITunesSearchApi::class.java)

    @ActivityScope
    @Provides
    fun providesDataRepository(iTunesSearchApi: ITunesSearchApi): DataRepository =
            DataRepository(iTunesSearchApi)

    @ActivityScope
    @Provides
    fun providesDashboardViewModelFactory(dataRepository: DataRepository,exoPlayer: ExoPlayer): DashboardViewModelFactory =
            DashboardViewModelFactory(dataRepository,exoPlayer)

    @ActivityScope
    @Provides
    fun providesViewModel(dashboardViewModelFactory: DashboardViewModelFactory): DashboardViewModel =
            ViewModelProviders
                    .of(dashboardActivity, dashboardViewModelFactory)
                    .get(DashboardViewModel::class.java)

    @ActivityScope
    @Provides
    fun providesPreviewListLayoutManager(): LinearLayoutManager = LinearLayoutManager(dashboardActivity)

    @ActivityScope
    @Provides
    fun providesPreviewListAdapter(dashboardViewModelView: DashboardViewModel): PreviewListAdapter =
            PreviewListAdapter(dashboardViewModelView.previewMusic,
                    dashboardViewModelView.playPauseState)

}

@Module
class ExoPlayerModule(private val context: Context) {

    @ActivityScope
    @Provides
    fun providesDefaultRenderersFactory(): DefaultRenderersFactory = DefaultRenderersFactory(context)

    @ActivityScope
    @Provides
    fun providesDefaultTrackSelector(): DefaultTrackSelector = DefaultTrackSelector()

    @ActivityScope
    @Provides
    fun providesDefaultLoadControl(): DefaultLoadControl = DefaultLoadControl()

    @ActivityScope
    @Provides
    fun providesExoPlayer(defaultRenderersFactory: DefaultRenderersFactory, defaultTrackSelector: DefaultTrackSelector,
                          loadControl: DefaultLoadControl): ExoPlayer = ExoPlayerFactory.newSimpleInstance(context, defaultRenderersFactory, defaultTrackSelector, loadControl)

    @ActivityScope
    @Provides
    fun providesDefaultHttpDataSourceFactory(): DefaultHttpDataSourceFactory = DefaultHttpDataSourceFactory("exoplayer")
}