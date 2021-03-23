package com.iti.elfarsisy.mad41.myapplication.ui.map

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.AddLocationLayoutBinding
import com.iti.elfarsisy.mad41.myapplication.databinding.FragmentMapBinding
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    //fragment Extension

    private lateinit var binding: FragmentMapBinding
    private lateinit var markerOptions: MarkerOptions
    private lateinit var addLocationLayoutBindingkbarLayout: AddLocationLayoutBinding

    //fragment Extension
    private val viewModel by viewModels<MapViewModel> {
        MapViewModelFactory(
            WeatherRepo(),
            SavedPlacesRepo(MyApplication.getContext()),
            UserSettingRepo(MyApplication.getContext()),
            MapFragmentArgs.fromBundle(requireArguments()).screenId
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        Timber.i("get sccreen Id arguments ====${MapFragmentArgs.fromBundle(requireArguments()).screenId}")
        markerOptions = MarkerOptions()
        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener {
                val markerOptions = MarkerOptions();
                markerOptions.position(it)
                markerOptions.title("${it.latitude},${it.longitude}")
                googleMap.clear()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 10f));
                googleMap.addMarker(markerOptions)
                viewModel.setLocation(it.latitude, it.longitude)
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mMapViewModel = viewModel
        binding.lifecycleOwner = this
        showCustomSnackBar()
        viewModel.navigatorBack.observe(viewLifecycleOwner, Observer { isNavigate ->
            if (isNavigate) {
                findNavController().popBackStack()
            }
        })
    }

    private fun showCustomSnackBar() {
        val snackbar = Snackbar.make(binding.mapContainerLayout, "", Snackbar.LENGTH_INDEFINITE)
        addLocationLayoutBindingkbarLayout =
            DataBindingUtil.inflate(layoutInflater, R.layout.add_location_layout, null, false)
        addLocationLayoutBindingkbarLayout.mMapViewModel = viewModel
        addLocationLayoutBindingkbarLayout.lifecycleOwner = this
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackbar.view as (Snackbar.SnackbarLayout)
        snackbarLayout.addView(addLocationLayoutBindingkbarLayout.root)
        snackbar.show()
    }


}