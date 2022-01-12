package com.appisode.doctorappointmentapp.ui.doctorlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appisode.doctorappointmentapp.R
import com.appisode.doctorappointmentapp.data.model.DoctorModel
import com.appisode.doctorappointmentapp.databinding.DoctorlistRowBinding
import com.appisode.doctorappointmentapp.util.DoctorsDiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.doctorlist_row.view.*

class DoctorListAdapter( private val urlListener: (doctorModel: DoctorModel) -> Unit) : RecyclerView.Adapter<DoctorListAdapter.MyViewHolder>(){

    var doctors = ArrayList<DoctorModel>()
    private lateinit var binding: DoctorlistRowBinding

    fun setDoctorListData(data:ArrayList<DoctorModel>){
        val diffUtil = DoctorsDiffUtil(this.doctors, data)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        this.doctors = data
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = DoctorlistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
       return doctors.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(doctors[position],urlListener)
    }

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        val doctorNameTV = view.doctorNameTextView
        val doctorImage = view.doctorImageView

        fun bind(doctor: DoctorModel, urlListener: (doctorModel: DoctorModel) -> Unit){
            doctorNameTV.text = doctor.full_name
            val url = doctor.image.url
            Glide.with(doctorImage)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.default_user)
                .error(R.drawable.default_user)
                .fallback(R.drawable.default_user)
                .into(doctorImage)

            view.setOnClickListener {
                urlListener.invoke(doctor)
            }
        }


    }
}