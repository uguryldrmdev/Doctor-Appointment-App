package com.appisode.doctorappointmentapp.network


import com.appisode.doctorappointmentapp.data.model.DoctorList
import retrofit2.Call

import retrofit2.http.GET

interface RetroService {

    @GET("android/doctors.json")
    fun getDataFromAPI(): Call<DoctorList>
}