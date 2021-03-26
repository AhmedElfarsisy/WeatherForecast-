package com.iti.elfarsisy.mad41.myapplication.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.SettingsFragmentBinding
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication

class SettingsFragment : Fragment() {

    //fragment Extension
    private val viewModel by viewModels<SettingsViewModel> {
        SettingViewModelFactory(UserSettingRepo(MyApplication.getContext()))
    }
    private lateinit var binding: SettingsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mSettingViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigatorToMap.observe(viewLifecycleOwner, Observer { isNavigate ->
            if (isNavigate) {
                val action = SettingsFragmentDirections.actionNavSettingsFragmentToMapFragment2(0)
                findNavController().navigate(action)
                viewModel.completNavigation()
            }
        })
    }

}