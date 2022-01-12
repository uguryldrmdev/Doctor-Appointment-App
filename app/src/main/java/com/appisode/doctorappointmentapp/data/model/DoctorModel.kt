package com.appisode.doctorappointmentapp.data.model

import com.google.gson.annotations.SerializedName

data class DoctorList(
    @SerializedName("doctors")
    val doctors: ArrayList<DoctorModel>
    )

data class DoctorModel(
    @SerializedName("full_name")
    val full_name: String,
    @SerializedName("user_status")
    val user_status: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: Image)

data class Image(
    @SerializedName("url")
    val url:String
    )
