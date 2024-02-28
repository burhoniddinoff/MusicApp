package com.example.musicapp.presentation.screen

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.Constants
import com.example.musicapp.R
import com.example.musicapp.databinding.ScreenHomeBinding
import com.example.musicapp.model.MusicData
import com.example.musicapp.presentation.adapter.MusicAdapter

class HomeScreen : Fragment(R.layout.screen_home) {

    private val binding by viewBinding(ScreenHomeBinding::bind)
    private var musicDataList = ArrayList<MusicData>()
    private lateinit var adapter: MusicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRecycler()
        checkUserPermissions()

    }

    @SuppressLint("Recycle")
    private fun loadSong() {
        val allSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val sorted = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = activity?.applicationContext?.contentResolver!!.query(
            allSongUri, null, selection, null, sorted
        )

        if (cursor != null) {

            while (cursor.moveToNext()) {
                val songUri =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val songAuthor =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val songName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                val songDuration =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)).toLong()

                musicDataList.add(
                    MusicData(songName, songAuthor, songUri, Constants.durationConverter(songDuration))
                )

            }

            cursor.close()

        }

    }

    private fun setUpRecycler() {
        adapter = MusicAdapter()

        binding.rvSongList.adapter = adapter
        binding.rvSongList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSongList.setHasFixedSize(true)
        binding.rvSongList.addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})

        adapter.submitList(musicDataList)
        musicDataList.clear()

    }

    private fun checkUserPermissions() {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                101
            )
            return
        }
        loadSong()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            101 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSong()
            } else {
                Toast.makeText(requireContext(), "Permission Denied, Add permission!!", Toast.LENGTH_SHORT).show()
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

