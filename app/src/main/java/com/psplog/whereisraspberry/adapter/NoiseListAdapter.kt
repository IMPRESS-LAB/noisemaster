package com.psplog.whereisraspberry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psplog.whereisraspberry.R
import com.psplog.whereisraspberry.dto.device.DeviceDTO
import com.psplog.whereisraspberry.dto.device.NoiseDTO

class NoiseListAdapter(var list: List<NoiseDTO.Data.Noise>) : RecyclerView.Adapter<NoiseListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_device_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tag.text = list[position].placeDTO.tag
        holder.deviceId.text = list[position].device
        holder.time.text = list[position].createdTime
        holder.decibel.text = list[position].decibel.toString()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag = itemView.findViewById<TextView>(R.id.tv_home_device_list_tag)
        val deviceId = itemView.findViewById<TextView>(R.id.tv_home_device_list_device_id)
        val decibel = itemView.findViewById<TextView>(R.id.tv_home_device_list_decibel)
        val time = itemView.findViewById<TextView>(R.id.tv_home_device_list_update_time)
    }
}