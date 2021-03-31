package com.iti.elfarsisy.mad41.myapplication.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.FragmentHomeBinding
import com.iti.elfarsisy.mad41.myapplication.helper.GPS_LOCATION_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.LOCATION_PERMISSION_ID
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber

class HomeFragment : Fragment() {
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    //fragment Extension
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            WeatherRepo(MyApplication.getContext()),
            UserSettingRepo(MyApplication.getContext())
        )
    }
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        binding.mHomeViewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.locationToolLive.observe(viewLifecycleOwner, Observer { liveTool ->
            if (liveTool.equals(GPS_LOCATION_VALUES)){
                getMyLocation()
            }else{
                homeViewModel.getLocation()
            }

        })


        return binding.root
    }

    //check if the user Give permissions to get his location
    private fun checkPermissions(): Boolean {
        var isPemissionsGranted = false
        if (ActivityCompat.checkSelfPermission(
                MyApplication.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPemissionsGranted = true
        }
        return isPemissionsGranted
    }


    // get Location Permissions from user (COARSE & FINE )
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_ID
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (checkPermissions()) {
            getMyLocation();
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestLocationData()
//                descriptionTV.setText("")
            } else {
                enableLocation()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 15000
        locationRequest.numUpdates = 1
        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            lastLocation = locationResult.lastLocation
            homeViewModel.setLatAndLong(lastLocation.latitude, lastLocation.longitude)
            Timber.i("${lastLocation.latitude}, ${lastLocation.longitude}}")
        }
    }

    private fun isLocationEnabled(): Boolean {
        val manager =
            MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun enableLocation() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }


}