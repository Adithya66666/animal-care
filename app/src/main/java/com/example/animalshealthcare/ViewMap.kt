package com.example.animalshealthcare

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.animalshealthcare.databinding.ActivityViewMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.example.animalshealthcare.R


class ViewMap : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var binding:ActivityViewMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private var currentLat:Double = 0.00
    private var currentLong:Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewMapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView.getMapAsync(this)

        animalLocation()

        binding.refresh.setOnClickListener{
            val label = "Animal Location" // replace with your label

            val geoUri = "geo:$currentLat,$currentLong?q=$currentLat,$currentLong($label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configure the map as needed
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Set up map click listener
        googleMap.setOnMapClickListener(this)

        // Check location permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Enable the My Location layer if the location permission is granted
            googleMap.isMyLocationEnabled = true

            // Request location updates
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val zoomLevel = 13.0f
                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(latitude, longitude))
                        .zoom(zoomLevel)
                        .build()
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMapClick(latLng: LatLng) {

    }

    // Example usage of the selected pickup location
    private fun processSelectedPickupLocation() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                onMapReady(googleMap)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }



    private fun animalLocation(){

        FirebaseDatabase.getInstance().getReference("location").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(this@ViewMap, snapshot.value.toString(), Toast.LENGTH_SHORT).show()
                var lat = snapshot.child("latitude").value.toString()
                var long = snapshot.child("longitude").value.toString()
                var location = LatLng(lat.toDouble(),long.toDouble())

                currentLat = lat.toDouble()
                currentLong = long.toDouble()

                addOrUpdateMarker(googleMap,applicationContext,location)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }



    var marker: Marker? = null

    fun addOrUpdateMarker(googleMap: GoogleMap, context: Context, position: LatLng) {
        val icon = BitmapFactory.decodeResource(context.resources,R.drawable.moving)
        val smallerIcon = getResizedBitmap(icon, 0.08f)

        if (marker == null) {
            val markerOptions = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromBitmap(smallerIcon))
                .anchor(0.5f, 0.5f) // Adjust the anchor point if needed

            marker = googleMap.addMarker(markerOptions)
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(position.latitude, position.longitude))
                .zoom(13.0f)
                .build()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        } else {
            marker?.position = position
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap, scale: Float): Bitmap {
        val width = (bitmap.width * scale).toInt()
        val height = (bitmap.height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }
}