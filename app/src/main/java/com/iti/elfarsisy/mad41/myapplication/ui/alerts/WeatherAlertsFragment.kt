package com.iti.elfarsisy.mad41.myapplication.ui.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.adapter.WeatherAlertsAdapter
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.databinding.AddWeatherAlertDialogLayoutBinding
import com.iti.elfarsisy.mad41.myapplication.databinding.CustomDialogLayoutBinding
import com.iti.elfarsisy.mad41.myapplication.databinding.WeatherAlertsFragmentBinding
import com.iti.elfarsisy.mad41.myapplication.helper.parseTimeStampToDate
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.util.*


class WeatherAlertsFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener, OnAlertsClickListener {
    private var pickedYear: Int? = null
    private var pickedMonth: Int? = null
    private var pickedDay: Int? = null
    val calendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askAboutPermissions()
    }

    private fun askAboutPermissions() {
        // Overlay permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            //when screen is black but not locked it will light-up
            requireActivity().setShowWhenLocked(true)
            requireActivity().setTurnScreenOn(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireActivity())) {
                checkDrawOverAppsPermissionsDialog()
            }
        }
        runBackgroundPermissions()
    }

    //fragment Extension
    private val viewModel by viewModels<WeatherAlertsViewModel> {
        WeatherAlertsViewModelFactory(
            WeatherRepo(MyApplication.getContext()),
            SavedPlacesRepo(MyApplication.getContext()),
            UserSettingRepo(MyApplication.getContext()),
            requireActivity()
        )
    }

    private lateinit var binding: WeatherAlertsFragmentBinding
    private lateinit var alertDialogLayoutBinding: AddWeatherAlertDialogLayoutBinding
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
        val adapter = WeatherAlertsAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setOnClick(this)
        viewModel.alertAddition.observe(viewLifecycleOwner, Observer { isShow ->
            if (isShow) {
                showAddAlertDialog()
            }
        })
    }

    //show custom Dialog to pick alert time from it
    private fun showAddAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialogLayoutBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.add_weather_alert_dialog_layout,
                null, false
            )
        alertDialogLayoutBinding.mAlertViewModel = viewModel
        alertDialogLayoutBinding.lifecycleOwner = this

        builder.setView(alertDialogLayoutBinding.root)
        val myAlert = builder.create()
        // get current time and parse it and print parsed timestamp into Text view
        val parseTimeStampToDate = parseTimeStampToDate(System.currentTimeMillis())
        alertDialogLayoutBinding.alertDateFrom.text = parseTimeStampToDate

        alertDialogLayoutBinding.alertDateTo.setOnClickListener {
            showDatePicker()
        }
        alertDialogLayoutBinding.addBtn.setOnClickListener {
            if (calendar.timeInMillis > System.currentTimeMillis())
                viewModel.insertAlert(
                    start = System.currentTimeMillis(),
                    end = calendar.timeInMillis
                )
            else
                Toast.makeText(
                    requireActivity(),
                    "please,Enter valid Date and Time",
                    Toast.LENGTH_SHORT
                ).show()
            myAlert.dismiss()
            viewModel.dismissAddAlertCompleted()
        }
        alertDialogLayoutBinding.cancelButton.setOnClickListener {
            myAlert.dismiss()
            viewModel.dismissAddAlertCompleted()
        }
        myAlert.window?.let { it.setBackgroundDrawable(ColorDrawable(0)) }

        myAlert.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Timber.i("@@@@@@@@@Date $year::::M:::: $month::::DOM::::: $dayOfMonth")
        pickedYear = year
        pickedMonth = month
        pickedDay = dayOfMonth
        showTimePicker()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        pickedYear?.let { year ->
            pickedMonth?.let { month ->
                pickedDay?.let { day ->
                    calendar.set(year, month, day, hourOfDay, minute, 0)
                }
            }
        }
        val parseTimeStampToDate = parseTimeStampToDate(calendar.timeInMillis)
        alertDialogLayoutBinding.alertDateTo.text = parseTimeStampToDate
        Timber.i(" @@@@@@@@@time     $hourOfDay:::M:::$minute ")
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireActivity(), this, year, month, day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            this, hour, minute, false
        )
        timePickerDialog.show()
    }

    override fun onDeleteAlert(alertId: Long) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val customDialogLayoutBinding: CustomDialogLayoutBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.custom_dialog_layout,
                null, false
            )
        customDialogLayoutBinding.mAlertModel = viewModel
        customDialogLayoutBinding.lifecycleOwner = this

        builder.setView(customDialogLayoutBinding.root)
        val myAlert = builder.create()
        customDialogLayoutBinding.deleteBtn.setOnClickListener {
            viewModel.delete(alertId)
            myAlert.dismiss()
        }
        customDialogLayoutBinding.cancelButton.setOnClickListener {
            myAlert.dismiss()
        }
        myAlert.window?.let { it.setBackgroundDrawable(ColorDrawable(0)) }

        myAlert.show()

    }


    private fun checkDrawOverAppsPermissionsDialog() {
        AlertDialog.Builder(requireActivity()).setTitle("Permission request").setCancelable(false)
            .setMessage("please allow Draw Over Apps permission to be able to use application properly")
            .setPositiveButton(
                "Yes"
            ) { dialog, which -> drawOverAppPermission() }.setNegativeButton(
                "No"
            ) { dialog, which -> errorWarningForNotGivingDrawOverAppsPermissions() }.show()
    }

    private fun errorWarningForNotGivingDrawOverAppsPermissions() {
        AlertDialog.Builder(requireActivity()).setTitle("Warning").setCancelable(false).setMessage(
            """
            Unfortunately the display over other apps permission is not granted so the application might not behave properly 
            To enable this permission kindly restart the application
            """.trimIndent()
        )
            .setPositiveButton("Ok") { dialog, which -> }.show()
    }

    fun runBackgroundPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (Build.BRAND.equals("xiaomi", ignoreCase = true)) {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
                startActivity(intent)
            } else if (Build.BRAND.equals(
                    "Honor",
                    ignoreCase = true
                ) || Build.BRAND.equals("HUAWEI", ignoreCase = true)
            ) {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
                startActivity(intent)
            }
        }
    }

    fun drawOverAppPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireActivity())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${requireActivity().packageName}")
                )
                startActivityForResult(intent, 80)
            }
        }
    }

}