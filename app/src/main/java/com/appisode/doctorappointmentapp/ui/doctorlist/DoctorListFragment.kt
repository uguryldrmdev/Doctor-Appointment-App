package com.appisode.doctorappointmentapp.ui.doctorlist


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appisode.doctorappointmentapp.R
import com.appisode.doctorappointmentapp.data.model.DoctorModel
import com.appisode.doctorappointmentapp.databinding.FragmentDoctorListBinding
import com.appisode.doctorappointmentapp.setDivider
import com.appisode.doctorappointmentapp.ui.doctorlist.adapter.DoctorListAdapter
import com.appisode.doctorappointmentapp.util.ConnectionLiveData
import com.appisode.doctorappointmentapp.util.Gender

class DoctorListFragment : Fragment() {

    private lateinit var binding: FragmentDoctorListBinding
    private lateinit var cld : ConnectionLiveData
    private lateinit var mDoctorListAdapter: DoctorListAdapter
    private lateinit var viewModel: DoctorListViewModel
    private lateinit var sm: FragmentManager
    private lateinit var rc: Context
    private var mDoctorList: ArrayList<DoctorModel>?=null
    private var mIsChoseGender: Boolean = false
    private var mChosenGender: String = getEmptyString()
    private var mText: String = getEmptyString()
    private var mIsWrittenEditText: Boolean = false

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

    override fun onResume() {
        super.onResume()
        checkNetworkConnection()
        binding.checkBoxMan.isChecked = false
        binding.checkBoxWoman.isChecked = false
        binding.editTextTextPersonName.text.clear()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })
        checkNetworkConnection()
    }
    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(requireActivity().application)

        cld.observe(viewLifecycleOwner, { isConnected ->

            if (isConnected){
                isUserFound(true)
                initDoctorsRecycleView()
                createDoctorList()
                searchDoctor()
            }else{
                isUserFound(false)
            }

        })
    }
    private fun initDoctorsRecycleView(){
        binding.doctorsRecycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            DoctorListAdapter{ doctor ->
                findNavController().navigate(
                    DoctorListFragmentDirections.actionDoctorListFragmentToDoctorDetailFragment(
                        doctor.full_name,
                        doctor.user_status,
                        doctor.image.url
                    )
                )
            }.also { mDoctorListAdapter = it }
            adapter = mDoctorListAdapter
            setDivider(R.drawable.recycler_view_divider)
        }
    }
    private fun createDoctorList(){
        viewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(DoctorListViewModel::class.java)

        viewModel.getDoctorListDataObserver().observe(viewLifecycleOwner,{

                mDoctorList = it?.doctors
            if (mDoctorList != null){
                mDoctorListAdapter.setDoctorListData(it.doctors)
                mDoctorList = it?.doctors
                isUserFound(true)
            }
            else{
                isUserFound(false)
            }


        })
        viewModel.getDoctors(){
            //...
        }

    }

    private fun searchDoctor(){
        filter()
        setCheckBoxSearch()
    }

    private fun setCheckBoxSearch(){
        setCheckBoxStatus(binding.checkBoxMan,binding.checkBoxWoman, Gender.MALE.genderStr)
        setCheckBoxStatus(binding.checkBoxWoman,binding.checkBoxMan,Gender.FEMALE.genderStr)
    }

    private fun setCheckBoxStatus(viewChecked: CheckBox, viewUnchecked: CheckBox, gender: String){
        viewChecked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                viewUnchecked.isChecked = false
                val mFilteredList = ArrayList<DoctorModel>()

                if(mDoctorList != null){
                    for (item in mDoctorList!!){
                        if (item.gender == gender){
                            if (mIsWrittenEditText){
                                if (item.full_name.lowercase().contains(mText.lowercase())){
                                    mFilteredList.add(item)
                                }
                            }else{
                                mFilteredList.add(item)
                            }

                        }
                    }
                    mIsChoseGender = true
                    mChosenGender = gender

                    if (mFilteredList.isNotEmpty()){
                        isUserFound(true)
                        mDoctorListAdapter.setDoctorListData(mFilteredList)
                    }else{
                        isUserFound(false)
                    }
                }else{
                    isUserFound(false)
                }

            }
            else{
                if(mDoctorList != null){
                    if (binding.editTextTextPersonName.text.isEmpty()){
                        mDoctorListAdapter.setDoctorListData(mDoctorList!!)
                        mIsChoseGender = false
                        mChosenGender = getEmptyString()
                    }else{
                        mIsChoseGender = false
                        mChosenGender = getEmptyString()
                        filterStatic()
                    }
                }
                else{
                    isUserFound(false)
                }
            }
        }
    }
    private fun filter(){
        binding.editTextTextPersonName.addTextChangedListener {text ->
            setEditTextSearch(text.toString())
            mText = text.toString()
            mIsWrittenEditText = mText != getEmptyString()
        }
    }

    private fun filterStatic(){
        setEditTextSearch(binding.editTextTextPersonName.text.toString())
    }
    private fun setEditTextSearch(text: String){
        val mFilteredList = ArrayList<DoctorModel>()

        if (mIsChoseGender){
            if(mDoctorList != null){
                for (item in mDoctorList!!){
                    if (item.full_name.lowercase().contains(text.lowercase())){
                        if (item.gender == mChosenGender){
                            mFilteredList.add(item)
                        }
                    }
                }
                if (mFilteredList.isNotEmpty()){
                    isUserFound(true)
                    mDoctorListAdapter.setDoctorListData(mFilteredList)

                }else{
                    isUserFound(false)
                }
            }
            else{
                isUserFound(false)
            }

        }
        else{
            if(mDoctorList != null){
                for (item in mDoctorList!!){
                    if (item.full_name.lowercase().contains(text.lowercase())){
                        mFilteredList.add(item)
                    }
                }
                if (mFilteredList.isNotEmpty()){
                    isUserFound(true)
                    mDoctorListAdapter.setDoctorListData(mFilteredList)

                }else{
                    isUserFound(false)
                }
            }else{
                isUserFound(false)
            }

        }

    }

    private fun isUserFound(isUserFound: Boolean){
        if(isUserFound){
            doVisible(binding.RecycleViewCL)
            doGone(binding.UserNotFoundCL)
        }
        else{
            doGone(binding.RecycleViewCL)
            doVisible(binding.UserNotFoundCL)
        }
    }

    private fun doGone(view: View){
        view.visibility = View.GONE
    }
    private fun doVisible(view: View){
        view.visibility = View.VISIBLE
    }

    private fun getEmptyString(): String = ""
}


