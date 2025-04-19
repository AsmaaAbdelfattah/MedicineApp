//package com.example.medicineremainder.Activities
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//import com.example.medicineremainder.Model.MapResponse
//import com.example.medicineremainder.R
//import com.example.medicineremainder.Utilities.MapApiRequest
//import com.example.medicineremainder.databinding.ActivityMapBinding
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
class MapActivity : AppCompatActivity() {
//    private lateinit var map: GoogleMap
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val tomTomApiKey = "QcnVKJx7guEK6Dp2f0EMuntnVkewHPQc"
//    lateinit var binding:ActivityMapBinding
//    private var locationPermissionGranted = false
//    private lateinit var lastKnownLocation: Location
//    // Define the constant for permission request code
//    companion object {
//        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMapBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//    }
//
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        updateLocationUI()
//        getDeviceLocation()
//    }
//    private fun getLocationPermission() {
//        // Check if the fine location permission is granted
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted = true
//        } else {
//            // If permission is not granted, request it
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        locationPermissionGranted = false
//        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                locationPermissionGranted = true
//            }
//        }
//        updateLocationUI() // Update the UI based on the permission result
//    }
//
//    private fun updateLocationUI() {
//        if (::map.isInitialized) {
//            try {
//                if (locationPermissionGranted) {
//                    map.isMyLocationEnabled = true
//                    getDeviceLocation() // Get the device location if permission is granted
//                } else {
//                    map.isMyLocationEnabled = false
//                    lastKnownLocation = Location("")
//                    getLocationPermission() // Request permission if not granted
//                }
//            } catch (e: SecurityException) {
//                Log.e("Exception: %s", e.message, e)
//            }
//        }
//    }
//
//    private fun getDeviceLocation() {
//        try {
//            if (locationPermissionGranted) {
//                val locationResult = fusedLocationClient.lastLocation
//                locationResult.addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful && task.result != null) {
//                        lastKnownLocation = task.result
//                        val currentLatLng = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                        // Call method to get nearby pharmacies
//
//                        fetchNearbyPharmacies(lastKnownLocation.latitude, lastKnownLocation.longitude)
//                    } else {
//                        Log.d("MapsActivity", "Current location is null. Using defaults.")
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }
//
//    private fun requestLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
//        } else {
//            getUserLocation()
//        }
//    }
//
////    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////            getUserLocation()
////        }
////    }
//
//    private fun getUserLocation() {
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                    if (location != null) {
//                        val userLatLng = LatLng(location.latitude, location.longitude)
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14f))
//                        fetchNearbyPharmacies(location.latitude, location.longitude)
//                    }
//                }
//            } else {
//                // Permission is not granted — maybe request it again or show a message
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
//            }
//    }
//    }
//
//    private fun fetchNearbyPharmacies(lat: Double, lon: Double) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.tomtom.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service = retrofit.create(MapApiRequest::class.java)
//        val call = service.getNearbyPlaces(
//            apiKey = tomTomApiKey,
//            lat = lat,
//            lon = lon,
//            radius = 3000,
//            categorySet = "7326"
//        )
//
//        call.enqueue(object : Callback<MapResponse> {
//            override fun onResponse(call: Call<MapResponse>, response: Response<MapResponse>) {
//                response.body()?.results?.forEach { result ->
//                    val position = LatLng(result.position.lat, result.position.lon)
//                    map.addMarker(
//                        MarkerOptions().position(position).title(result.poi.name ?: "Pharmacy")
//                    )
//                }
//            }
//
//            override fun onFailure(call: Call<MapResponse>, t: Throwable) {
//                Toast.makeText(this@MapActivity, "Failed: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}