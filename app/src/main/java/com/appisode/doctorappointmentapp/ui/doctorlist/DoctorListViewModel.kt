package com.appisode.doctorappointmentapp.ui.doctorlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appisode.doctorappointmentapp.data.model.DoctorList
import com.appisode.doctorappointmentapp.network.RetroInstance
import com.appisode.doctorappointmentapp.network.RetroService
import retrofit2.Call

class DoctorListViewModel:ViewModel() {
    var mDoctorListData: MutableLiveData<DoctorList> = MutableLiveData()


    fun getDoctorListDataObserver(): MutableLiveData<DoctorList>{
        return mDoctorListData
    }
    fun getDoctors(responseCallback: (Boolean) -> Unit){
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call =retroInstance.getDataFromAPI()
        call.enqueue(object : retrofit2.Callback<DoctorList>{
            override fun onResponse(
                call: Call<DoctorList>,
                response: retrofit2.Response<DoctorList>
            ) {
                if (response.isSuccessful){
                    responseCallback(true)
                    mDoctorListData.postValue(response.body())
                }else{
                    mDoctorListData.postValue(null)
                }
            }

            override fun onFailure(call: Call<DoctorList>?, t: Throwable) {
                responseCallback(false)
                mDoctorListData.postValue(null)
            }
        })
    }


}
