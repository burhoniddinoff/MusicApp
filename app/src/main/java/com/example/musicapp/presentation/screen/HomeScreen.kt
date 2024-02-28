package com.example.musicapp.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.databinding.ScreenHomeBinding
import com.example.musicapp.presentation.service.MyService

class HomeScreen : Fragment(R.layout.screen_home) {

    private val binding by viewBinding(ScreenHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val intent = Intent(requireContext(), MyService::class.java)

        binding.hello1.setOnClickListener {
            requireContext().startService(intent)
        }

        binding.hello2.setOnClickListener {
            requireContext().stopService(intent)
        }

    }
}