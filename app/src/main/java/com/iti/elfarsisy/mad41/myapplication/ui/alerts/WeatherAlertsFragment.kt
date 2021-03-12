package com.iti.elfarsisy.mad41.myapplication.ui.alerts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.databinding.WeatherAlertsFragmentBinding

class WeatherAlertsFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherAlertsFragment()
    }

    private lateinit var viewModel: WeatherAlertsViewModel
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
        viewModel = ViewModelProvider(this).get(WeatherAlertsViewModel::class.java)
        binding.mAlertsViewModel = viewModel
        binding.lifecycleOwner = this
    }

}