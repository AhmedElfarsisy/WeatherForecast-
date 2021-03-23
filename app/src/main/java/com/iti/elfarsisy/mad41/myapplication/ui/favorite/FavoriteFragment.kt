package com.iti.elfarsisy.mad41.myapplication.ui.favorite

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.adapter.FavoritePlacesAdapter
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.CustomDialogLayoutBinding
import com.iti.elfarsisy.mad41.myapplication.databinding.FavoriteFragmentBinding
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication


class FavoriteFragment : Fragment(), OnFavoriteClickListner {
    //fragment Extension
    private val viewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory(
            WeatherRepo(),
            SavedPlacesRepo(MyApplication.getContext()),
            UserSettingRepo(MyApplication.getContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var binding: FavoriteFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mFavoriteViewModel = viewModel
        binding.lifecycleOwner = this
        val adapter = FavoritePlacesAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setOnClick(this)

        viewModel.navigatorToMap.observe(viewLifecycleOwner, Observer { isNavigate ->
            if (isNavigate) {
                val action = FavoriteFragmentDirections.actionNavFavoriteFragmentToMapFragment2(1)
                findNavController().navigate(action)
                viewModel.completeNavigation();
            }
        })

        viewModel.navigateToFavoriteDetail.observe(viewLifecycleOwner, Observer { pairNavigation ->
            if (pairNavigation.first != null && pairNavigation.second != null) {
                val lat = pairNavigation.first!!.toFloat()
                val lon = pairNavigation.second!!.toFloat()
                val action =
                    FavoriteFragmentDirections.actionNavFavoriteFragmentToFavoriteDetailsFragment(lat, lon)
                findNavController().navigate(action)
                viewModel.completeNavigationToDetails();
            }
        })
    }

    override fun onPlaceClick(placeId: Int) {
        viewModel.navigateToFavoritePlace(placeId)
    }

    override fun onDeletePlace(placeId: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val customDialogLayoutBinding: CustomDialogLayoutBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.custom_dialog_layout,
                null, false
            )
        customDialogLayoutBinding.mFavModel = viewModel
        customDialogLayoutBinding.lifecycleOwner = this

        builder.setView(customDialogLayoutBinding.root)
        val myAlert = builder.create()
        customDialogLayoutBinding.deleteBtn.setOnClickListener {
            viewModel.delete(placeId)
            myAlert.dismiss()
        }
        customDialogLayoutBinding.cancelButton.setOnClickListener {
            myAlert.dismiss()
        }
        if (myAlert.window != null) {
            myAlert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        myAlert.show()

    }

    override fun onStart() {
        super.onStart()
        viewModel.refershUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refershUI()

    }

}