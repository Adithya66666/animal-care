package com.example.animalshealthcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class DeviceAdapter(private val DeviceList:ArrayList<Device>, private val listener: Dashboard): RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_element,parent,false)
        return DeviceViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val currentItem = DeviceList[position]
        var currentNumber = position + 1
        holder.deviceName.text = "Device $currentNumber"
        holder.deviceStatus.text = "ON"

    }
    override fun getItemCount(): Int {
        return DeviceList.size
    }

    inner class DeviceViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val deviceName: TextView = itemView.findViewById(R.id.deviceName)
        val deviceStatus: TextView = itemView.findViewById(R.id.deviceStatus)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}