package com.example.musicapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicData(
    val songTitle: String,
    val songArtist: String,
    val songUri: String,
    val songDuration: String,
) : Parcelable