package com.iti.elfarsisy.mad41.myapplication.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.iti.elfarsisy.mad41.myapplication.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private lateinit var markerOptions: MarkerOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
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

            }
        }


//        OnMapReadyCallback() {googleMap->
//            googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener {latLng->
//                markerOptions.position(latLng)
//                markerOptions.title(latLng.toString())
//                googleMap.clear();
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
//            })
//            googleMap.addMarker(markerOptions)
//        })


        return view.rootView
    }

    override fun onMapReady(googleMap: GoogleMap?) {

    }


}