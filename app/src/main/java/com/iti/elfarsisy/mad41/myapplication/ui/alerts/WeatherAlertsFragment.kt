package com.iti.elfarsisy.mad41.myapplication.ui.alerts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.WeatherAlertsFragmentBinding
import com.iti.elfarsisy.mad41.myapplication.ui.favorite.FavoriteViewModel
import com.iti.elfarsisy.mad41.myapplication.ui.favorite.FavoriteViewModelFactory
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication

class WeatherAlertsFragment : Fragment() {
    //fragment Extension
    private val viewModel by viewModels<WeatherAlertsViewModel> {
        WeatherAlertsViewModelFactory(WeatherRepo(), UserSettingRepo(MyApplication.getContext()))
    }

    companion object {
        fun newInstance() = WeatherAlertsFragment()
    }

    private lateinit var binding: WeatherAlertsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.weather_alerts_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mAlertsViewModel = viewModel
        binding.lifecycleOwner = this
    }

}