package com.appisode.doctorappointmentapp.ui.appointment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.appisode.doctorappointmentapp.R
import com.appisode.doctorappointmentapp.databinding.FragmentAppointmentBinding


class AppointmentFragment() : Fragment() {

    private lateinit var binding: FragmentAppointmentBinding
    private lateinit var sm: FragmentManager
    private lateinit var rc: Context
    private fun initDefaults() {
        sm = requireActivity().supportFragmentManager
        rc = requireContext()
        setStatusBarColor()
    }
    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            this.resources.getColor(R.color.tabColorGreen, requireActivity().theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDefaults()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}