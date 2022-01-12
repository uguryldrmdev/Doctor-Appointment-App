package com.appisode.doctorappointmentapp.ui.doctordetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appisode.doctorappointmentapp.R
import com.appisode.doctorappointmentapp.databinding.FragmentDoctorDetailBinding
import com.appisode.doctorappointmentapp.util.MembershipStatus
import com.bumptech.glide.Glide


class DoctorDetailFragment() : Fragment() {

    private lateinit var binding: FragmentDoctorDetailBinding
    private lateinit var sm: FragmentManager
    private lateinit var rc: Context
    private val args: DoctorDetailFragmentArgs by navArgs()
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
        binding = FragmentDoctorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDoctorInfo()
        setClicks()
    }

    private fun setDoctorInfo(){
        binding.textViewNameDoctorDetail. text = args.userName
        Glide.with(binding.imageViewDoctorDetail)
            .load(args.userImage)
            .circleCrop()
            .placeholder(R.drawable.default_user)
            .error(R.drawable.default_user)
            .fallback(R.drawable.default_user)
            .into(binding.imageViewDoctorDetail)
        setVisibilities()
    }

    private fun setVisibilities(){
        when(args.userStatus){
            MembershipStatus.FREE.statusStr -> {
              setVisibilitiesStatus(false)
            }
            MembershipStatus.PREMIUM.statusStr -> {
                setVisibilitiesStatus(true)
            }
        }
    }

    private fun setClicks(){
        binding.PremiumAlCardView.setOnClickListener {
            findNavController().navigate(DoctorDetailFragmentDirections.actionDoctorDetailFragmentToPaymentFragment())
        }
        binding.RandevuAlCardView.setOnClickListener {
            findNavController().navigate(DoctorDetailFragmentDirections.actionDoctorDetailFragmentToAppointmentFragment())
        }
    }

    private fun doGone(view: View){
        view.visibility = View.GONE
    }
    private fun doVisible(view: View){
        view.visibility = View.VISIBLE
    }

    private fun setVisibilitiesStatus(isPremium: Boolean){
        if (isPremium){
            doVisible(binding.textViewPremiumDoctorDetail)
            doGone(binding.PremiumAlCardView)
            doVisible(binding.RandevuAlCardView)
        }else{
            doGone(binding.textViewPremiumDoctorDetail)
            doVisible(binding.PremiumAlCardView)
            doGone(binding.RandevuAlCardView)
        }
    }

}