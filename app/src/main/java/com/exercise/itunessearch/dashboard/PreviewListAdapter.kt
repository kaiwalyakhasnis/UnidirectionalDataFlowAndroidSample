package com.exercise.itunessearch.dashboard

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exercise.itunessearch.R

import com.exercise.itunessearch.common.GlideApp
import com.exercise.itunessearch.common.SingleLiveEvent
import com.exercise.itunessearch.network.PreviewItem
import kotlinx.android.synthetic.main.preview_list_empty_item.view.*
import kotlinx.android.synthetic.main.preview_list_item.view.*

class PreviewListAdapter(private val previewMusicSingleEvent: SingleLiveEvent<Pair<Int, String?>>,
                         private val playPauseState: SparseBooleanArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previewList: List<PreviewListModel?>? = null

    fun setUpPreviewItems(previewList: List<PreviewListModel?>?) {
        this.previewList = previewList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        previewListItem -> {
            PreviewListHolders.PreviewListItemViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.preview_list_item, parent, false))
        }
        previewListEmpty -> {
            PreviewListHolders.EmptyListItemViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.preview_list_empty_item, parent, false))
        }
        else -> {
            null!!
        }
    }

    override fun getItemCount(): Int = previewList?.size ?: 0

    override fun getItemViewType(position: Int): Int = previewList?.get(position)?.getType()
            ?: previewListEmpty

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            previewListItem -> {
                val item = (previewList?.get(position) as PreviewItem)
                (holder as PreviewListHolders.PreviewListItemViewHolder)
                        .itemView
                        .also {
                            it.play_pause.isEnabled = playPauseState.get(position, false)
                        }
                        .also {
                            GlideApp.with(it.context)
                                    .load(item.artworkUrl100)
                                    .into(it.preview_image)
                            it.track_name.text = item.trackName ?: item.collectionCensoredName
                        }.also { itetmView ->
                            itetmView.preview_container
                                    .setOnClickListener {
                                        if (!playPauseState.get(position, false)) {
                                            playPauseState.put(position, false)
                                        }
                                        previewMusicSingleEvent.value = Pair(position, item.previewUrl)
                                    }
                        }

            }
            previewListEmpty -> {
                (holder as PreviewListHolders.EmptyListItemViewHolder)
                        .itemView
                        .let {
                            it.empty_text.text = it.context.getString(R.string.previews_not_available)
                        }
            }
        }
    }

    sealed class PreviewListHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class PreviewListItemViewHolder(itemView: View) : PreviewListHolders(itemView)
        class EmptyListItemViewHolder(itemView: View) : PreviewListHolders(itemView)
    }

    companion object {
        const val previewListItem = 0
        const val previewListEmpty = 1
    }

}