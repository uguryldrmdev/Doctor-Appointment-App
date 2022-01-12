package com.appisode.doctorappointmentapp.util

import androidx.recyclerview.widget.DiffUtil
import com.appisode.doctorappointmentapp.data.model.DoctorModel

class DoctorsDiffUtil(
    private val oldList: ArrayList<DoctorModel>,
    private val newList: ArrayList<DoctorModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].image.url == newList[newItemPosition].image.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].full_name != newList[newItemPosition].full_name-> false
            oldList[oldItemPosition].image.url != newList[newItemPosition].image.url -> false
            else -> true
        }
    }
}