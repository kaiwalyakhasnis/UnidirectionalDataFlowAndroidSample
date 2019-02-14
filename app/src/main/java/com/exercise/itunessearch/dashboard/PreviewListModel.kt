package com.exercise.itunessearch.dashboard

import com.exercise.itunessearch.dashboard.PreviewListAdapter.Companion.previewListEmpty

interface PreviewListModel {
    fun getType(): Int
}

class PreviewListEmpty : PreviewListModel {
    override fun getType(): Int = previewListEmpty
}