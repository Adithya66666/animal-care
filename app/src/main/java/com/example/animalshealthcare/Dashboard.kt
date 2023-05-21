package com.example.animalshealthcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalshealthcare.databinding.ActivityDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var deviceArray : ArrayList<Device>
    private lateinit var bmiRecyclerView : RecyclerView

    private var deviceIdArray = arrayListOf<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.refresh.setOnClickListener{
            readData()
        }

        bmiRecyclerView = binding.deviceList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        deviceArray = arrayListOf<Device>()

        readData()

    }

    private fun readData(){
        deviceArray.clear()
        deviceIdArray.clear()
        binding.deviceLoading.visibility = View.VISIBLE
        binding.deviceList.visibility = View.GONE
        FirebaseDatabase.getInstance().getReference("").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){

                        deviceIdArray += fineSnapshot.key.toString()

                        val deviceItem =  fineSnapshot.getValue(Device::class.java)
                        deviceArray.add(deviceItem!!)
                    }
                    bmiRecyclerView.adapter = DeviceAdapter(deviceArray,this@Dashboard)
                }
                binding.deviceLoading.visibility = View.GONE
                binding.deviceList.visibility = View.VISIBLE
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        var intent = Intent(this,ViewDevice::class.java).also {
            it.putExtra("deviceId",deviceIdArray?.get(position))
            it.putExtra("deviceName",position)
        }
        startActivity(intent)
    }
}