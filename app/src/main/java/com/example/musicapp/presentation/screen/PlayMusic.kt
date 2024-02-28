package com.example.musicapp.presentation.screen

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.Constants
import com.example.musicapp.R
import com.example.musicapp.databinding.ScreenPlayMusicBinding
import com.example.musicapp.model.MusicData

class PlayMusic : Fragment(R.layout.screen_play_music) {

    private val binding by viewBinding(ScreenPlayMusicBinding::bind)
    private val navArgs: PlayMusicArgs by navArgs()
    private lateinit var data: MusicData
    private var mediaPlayer: MediaPlayer? = null
    private var seekLength: Int = 0
    private val seekForwardTime = 5000
    private val seekBackwardTime = 5000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        data = navArgs.data
        initViews()

    }

    private fun initViews() {
        mediaPlayer = MediaPlayer()

        binding.apply {
            tvAuthor.text = data.songArtist
            tvTitle.text = data.songTitle
            tvDuration.text = data.songDuration
        }

        playSong()

        binding.ibPlay.setOnClickListener {
            playSong()
        }


    }

    private fun playSong() = if (!mediaPlayer!!.isPlaying) {
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(data.songUri)
        mediaPlayer!!.prepare()
        mediaPlayer!!.seekTo(seekLength)
        mediaPlayer!!.start()

        binding.ibPlay.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))

        updateSeekBar()

    } else {
        mediaPlayer!!.pause()
        seekLength = mediaPlayer!!.currentPosition
        binding.ibPlay.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_play
            )
        )
    }

    private fun updateSeekBar() {
        if (mediaPlayer != null) binding.tvCurrentTime.text =
            Constants.durationConverter(mediaPlayer!!.currentPosition.toLong())

        if (mediaPlayer != null) {
            binding.seekBar.progress = mediaPlayer!!.currentPosition
            binding.seekBar.max = mediaPlayer!!.duration
        }

        binding.seekBar.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView")

        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer!!.seekTo(progress)
                    binding.tvCurrentTime.text = Constants.durationConverter(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer!!.isPlaying) {
                    if (seekBar != null) {
                        mediaPlayer!!.seekTo(seekBar.progress)
                    }
                }
            }
        })

        Handler(Looper.getMainLooper()!!).postDelayed(runnable, 50)
    }

    private fun clearMediaPlayer() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
        }
        mediaPlayer!!.release()
        mediaPlayer = null
    }

    private var runnable = Runnable { updateSeekBar() }

    override fun onDestroy() {
        super.onDestroy()
        clearMediaPlayer()
    }

}