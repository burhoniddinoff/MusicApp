package com.example.musicapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.model.MusicData

class MusicAdapter : ListAdapter<MusicData, MusicAdapter.InnerHolder>(InnerDiffUtil) {

    var onClick: ((MusicData) -> Unit)? = null

    inner class InnerHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: MusicData, pos: Int) {

            binding.apply {
                tvDuration.text = song.songDuration
                songTitle.text = song.songTitle
                songArtist.text = song.songArtist
                tvOrder.text = "${pos.plus(1)}"
            }

        }

        init {
            binding.root.setOnClickListener {
                onClick?.invoke(getItem(adapterPosition))

            }
        }

    }

    object InnerDiffUtil : DiffUtil.ItemCallback<MusicData>() {
        override fun areItemsTheSame(oldItem: MusicData, newItem: MusicData): Boolean {
            return oldItem.songTitle == newItem.songTitle
        }

        override fun areContentsTheSame(oldItem: MusicData, newItem: MusicData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) = holder.bind(getItem(position), position)
}