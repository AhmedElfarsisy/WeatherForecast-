package com.iti.elfarsisy.mad41.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    //fragment Extension
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(WeatherRepo())
    }
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.mHomeViewModel = homeViewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}