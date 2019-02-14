package com.exercise.itunessearch.dashboard

import android.app.Activity
import android.arch.lifecycle.Observer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.exercise.itunessearch.ITunesSearchApplication
import com.exercise.itunessearch.R
import com.exercise.itunessearch.common.OkHttp3IdlingResource
import com.exercise.itunessearch.common.State
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import javax.inject.Inject


class DashboardActivity : AppCompatActivity() {

    @Inject
    lateinit var dashboardViewModel: DashboardViewModel

    @Inject
    lateinit var previewListAdapter: PreviewListAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var dataSourceFactory: DefaultHttpDataSourceFactory

    //for expresso test
    @Inject
    lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        DaggerDashboardComponent
                .builder()
                .appComponent((application as ITunesSearchApplication).appComponent)
                .dashboardModule(DashboardModule(this))
                .exoPlayerModule(ExoPlayerModule(this))
                .build()
                .inject(this)

        setUpView()

        dashboardViewModel.player.playWhenReady = true
    }

    override fun onStart() {
        super.onStart()
        dashboardViewModel
                .previewList
                .observe(this, Observer { stateMachine ->
                    when (stateMachine?.State) {
                        State.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            preview_list.visibility = View.VISIBLE
                            previewListAdapter.setUpPreviewItems(stateMachine.data)
                        }
                        State.LOADING -> {
                            preview_list.visibility = View.GONE
                            progressBar.visibility = View.VISIBLE
                        }
                        State.ERROR -> {
                            progressBar.visibility = View.GONE
                            Snackbar.make(dashboard_container, stateMachine.message.toString(),
                                    Snackbar.LENGTH_INDEFINITE)
                                    .let { snackbar ->
                                        snackbar.setAction(getString(R.string.retry)) {
                                            retrievePreviewList()
                                            snackbar.dismiss()
                                        }
                                    }.show()
                        }
                    }
                })

        dashboardViewModel
                .previewMusic
                .observe(this, Observer {
                    dashboardViewModel.player.stop(true)

                    val playPauseState = dashboardViewModel.playPauseState
                    for (i in 0 until playPauseState.size()) {
                        val key = playPauseState.keyAt(i)
                        if (key == it!!.first && !playPauseState.get(it.first, false)) {
                            dashboardViewModel.playPauseState.put(playPauseState.keyAt(i), true)
                            dashboardViewModel.player.prepare(ExtractorMediaSource.Factory(dataSourceFactory)
                                    .createMediaSource(Uri.parse(it.second)))
                        } else {
                            dashboardViewModel.playPauseState.put(playPauseState.keyAt(i), false)
                        }
                        previewListAdapter.notifyItemChanged(key)
                    }
                })
    }

    private fun setUpView() {
        preview_list.setHasFixedSize(true)
        preview_list.layoutManager = linearLayoutManager
        preview_list.adapter = previewListAdapter

        btn_search.setOnClickListener {
            hideSoftKeyboard()
            dashboardViewModel.playPauseState.clear()
            retrievePreviewList()
        }
    }

    //can be moved to extension function
    private fun hideSoftKeyboard() {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(toolbar.windowToken, 0)
    }

    private fun retrievePreviewList() {
        val searchTerm = edt_search_term.text.toString()
        if (searchTerm.isNotEmpty()) {
            search_container.isErrorEnabled = false
            dashboardViewModel.retrievePreviewList(searchTerm)
        } else {
            search_container.error = getString(R.string.search_term_error)
            search_container.isErrorEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        dashboardViewModel
                .previewList
                .removeObservers(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            dashboardViewModel.player.release()
        }
    }

}
