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

    //current values
        private var humidity = 0
        private var temp = 0
        private var acceleroX = 0
        private var acceleroY = 0
        private var acceleroZ = 0
        private var gyroscopeX = 0
        private var gyroscopeY = 0
        private var gyroscopeZ = 0
        private var rain:Boolean = false


        override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewDeviceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val position = "0"

        val updatedPosition = (position?.toInt() ?: 0) + 1

        binding.deviceName.text = "Device $updatedPosition"

        binding.subName.text = "Device Dashboard of the Device $updatedPosition"

        binding.textView9.text = "Device $updatedPosition Details"

        readData()

        binding.refresh.setOnClickListener{
            //readData()
        }

        binding.viewLocation.setOnClickListener{
            //val latitude = 6.8218900508952744 // replace with your latitude value
            //val longitude = 80.04007030912736 // replace with your longitude value

            /*
            val latitude = 6.8218900508952744 // replace with your latitude value
            val longitude = 80.04007030912736 // replace with your longitude value
            val label = "My Location" // replace with your label

            val geoUri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent) */

            var intent = Intent(this,ViewMap::class.java)
            startActivity(intent)

        }

        //accelo onchange
        FirebaseDatabase.getInstance().getReference("environment").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var humidity = snapshot.child("humidity").value.toString()
                var temp = snapshot.child("temperature").value.toString()

                binding.tempValue.text = temp

                if(temp.toFloat() >= 50){
                    binding.tempStatus.text = "Higher Temperature"
                }else{
                    binding.tempStatus.text = "Normal Temperature"
                }

                if(humidity.toFloat() > 80){
                    binding.humidityStatus.text = "Higher Humidity"
                }else{
                    binding.humidityStatus.text = "Normal Humidity"
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        FirebaseDatabase.getInstance().getReference("sensors").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var ax = snapshot.child("accelerometer/x").value.toString()
                var ay = snapshot.child("accelerometer/y").value.toString()
                var az = snapshot.child("accelerometer/z").value.toString()

                var gx = snapshot.child("gyroscope/x").value.toString()
                var gy = snapshot.child("gyroscope/y").value.toString()
                var gz = snapshot.child("gyroscope/z").value.toString()

                var rain = snapshot.child("rain").value.toString()

                binding.accelStatus.text = "Not Moving"

                if(rain == "true"){
                    binding.rainStatus.text = "Raining"
                }else{
                    binding.rainStatus.text = "Not Raining"
                }

                if((acceleroX + 100) < ax.toInt() || (acceleroY + 100) < ay.toInt() || (acceleroZ + 100) < az.toInt() || (gyroscopeX + 100) < gx.toInt() ||
                    (gyroscopeY + 100) < gy.toInt() || (gyroscopeZ + 100) < gz.toInt()){
                    binding.accelStatus.text = "Moving"
                }else{
                    binding.accelStatus.text = "Not Moving"
                }
                acceleroX = ax.toInt()
                acceleroY = ay.toInt()
                acceleroZ = az.toInt()
                gyroscopeX = gx.toInt()
                gyroscopeY = gy.toInt()
                gyroscopeZ = gz.toInt()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun readData(){
        FirebaseDatabase.getInstance().getReference("environment").get().addOnSuccessListener {
            if (it.exists()) {

                var humidity = it.child("humidity").value.toString()
                var temp = it.child("temperature").value.toString()

                binding.tempValue.text = temp

                if (temp.toFloat() >= 50) {
                    binding.tempStatus.text = "Higher Temperature"
                } else {
                    binding.tempStatus.text = "Normal Temperature"
                }

                if (humidity.toFloat() > 80) {
                    binding.humidityStatus.text = "Higher Humidity"
                } else {
                    binding.humidityStatus.text = "Normal Humidity"
                }
            }
        }


        FirebaseDatabase.getInstance().getReference("sensors").get().addOnSuccessListener {
            if (it.exists()) {
                var ax = it.child("accelerometer/x").value.toString()
                var ay = it.child("accelerometer/y").value.toString()
                var az = it.child("accelerometer/z").value.toString()

                var gx = it.child("gyroscope/x").value.toString()
                var gy = it.child("gyroscope/y").value.toString()
                var gz = it.child("gyroscope/z").value.toString()

                var rain = it.child("rain").value.toString()

                binding.accelStatus.text = "Not Moving"

                if(rain == "true"){
                    binding.rainStatus.text = "Raining"
                }else{
                    binding.rainStatus.text = "Not Raining"
                }

                acceleroX = ax.toInt()
                acceleroY = ay.toInt()
                acceleroZ = az.toInt()
                gyroscopeX = gx.toInt()
                gyroscopeY = gy.toInt()
                gyroscopeZ = gz.toInt()
            }
        }
    }


}