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
            Toast.makeText(this, "Data refreshed", Toast.LENGTH_SHORT).show()
        }

        bmiRecyclerView = binding.deviceList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        deviceArray = arrayListOf<Device>()
        
        binding.device01.setOnClickListener{
            var intent = Intent(this,ViewDevice::class.java)
            startActivity(intent)
        }
        
    }
}