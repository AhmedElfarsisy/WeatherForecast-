package com.iti.elfarsisy.mad41.myapplication.ui.favoritedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.FavoriteDetailsFragmentBinding
import com.iti.elfarsisy.mad41.myapplication.helper.showError
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication

class FavoriteDetailsFragment : Fragment() {

    //fragment Extension
    private val viewModel by viewModels<FavoriteDetailsViewModel> {
        FavoriteDetailsViewModelFactory(
            WeatherRepo(MyApplication.getContext()),
            FavoriteDetailsFragmentArgs.fromBundle(requireArguments()).lat,
            FavoriteDetailsFragmentArgs.fromBundle(requireArguments()).lon,
            UserSettingRepo(MyApplication.getContext())
        )
    }
    private lateinit var binding: FavoriteDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.favorite_details_fragment, container, false)
        binding.mViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isOnlineLive.observe(viewLifecycleOwner, Observer { isOnline ->
                if (isOnline){
                    showError("Network Error please check connection",requireView())
                }
        })

        return binding.root
    }
}