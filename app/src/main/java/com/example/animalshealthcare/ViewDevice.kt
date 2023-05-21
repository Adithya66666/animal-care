    package com.example.animalshealthcare

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.animalshealthcare.databinding.ActivityDashboardBinding
import com.example.animalshealthcare.databinding.ActivityViewDeviceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

    class ViewDevice : AppCompatActivity() {
    private lateinit var binding: ActivityViewDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewDeviceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val deviceId = intent.getStringExtra("deviceId")
        val position = intent.getStringExtra("deviceName")

        val updatedPosition = (position?.toInt() ?: 0) + 1

        binding.deviceName.text = "Device $updatedPosition"

        binding.subName.text = "Device Dashboard of the Device $updatedPosition"

        binding.textView9.text = "Device $updatedPosition Details"

        readData(deviceId.toString())

        binding.refresh.setOnClickListener{
            readData(deviceId.toString())
        }

        binding.viewLocation.setOnClickListener{
            //val latitude = 6.8218900508952744 // replace with your latitude value
            //val longitude = 80.04007030912736 // replace with your longitude value

            val latitude = 6.8218900508952744 // replace with your latitude value
            val longitude = 80.04007030912736 // replace with your longitude value
            val label = "My Location" // replace with your label

            val geoUri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }

        //accelo onchange
        FirebaseDatabase.getInstance().getReference(deviceId.toString()).child("").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach{
                    readData(deviceId.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun readData(deviceId:String){
        binding.deviceLoading.visibility = View.VISIBLE
        //binding.viewData.visibility = View.GONE
        FirebaseDatabase.getInstance().getReference(deviceId).get().addOnSuccessListener {
            if(it.exists()){

                var accel = it.child("ACCELO").value.toString()
                var gps = it.child("GPS").value.toString()
                var humidity = it.child("HUMIDITY").value.toString()
                var rain = it.child("Rain").value.toString()
                var temp = it.child("TEMPERATURE").value.toString()


                var isMoving = false
                var isTempHigh = false
                var isRain = false
                var isHumidity = false

                //if the temp is higher than 100 send warning
                if(temp.toInt() >= 100){
                    isTempHigh = true
                }

                //if rain is 0 = raining
                if(rain.toInt() == 0){
                    isRain = true
                }

                //accel > 200 = animal is moving
                if(accel.toInt() > 200){
                    isMoving = true
                }

                //if 80% < humidity
                if(humidity.toInt() > 80){
                    isHumidity = true
                }

                if(isTempHigh){
                    binding.tempStatus.text = "Higher Temperature"
                }else{
                    binding.tempStatus.text = "Normal Temperature"
                }

                if(isRain){
                    binding.rainStatus.text = "Raining"
                }else{
                    binding.rainStatus.text = "Not Raining"
                }

                if(isMoving){
                    binding.accelStatus.text = "Moving"
                }else{
                    binding.accelStatus.text = "Stopped"
                }

                if(isHumidity){
                    binding.humidityStatus.text = "Higher Humidity"
                }else{
                    binding.humidityStatus.text = "Normal Humidity"
                }


                binding.accel.text = "Accelerometer $accel"
                binding.humidity.text = "Humidity $humidity"
                binding.location.text = "Location"
                binding.rain.text = "Water level $rain"
                binding.temp.text = "Temperature $temp"
                binding.deviceLoading.visibility = View.GONE
                binding.viewData.visibility = View.VISIBLE
            }else{
                binding.deviceLoading.visibility = View.GONE
                binding.dataNotFound.visibility = View.VISIBLE
            }
        }
    }


}