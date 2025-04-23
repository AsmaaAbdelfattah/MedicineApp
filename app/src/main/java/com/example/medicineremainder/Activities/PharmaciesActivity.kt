package com.example.medicineremainder.Activities
//


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.medicineremainder.Model.Constants
import com.example.medicineremainder.Model.GeoapifyResponse
import com.example.medicineremainder.Utilities.GeoapifyService
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PharmaciesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkPermissionsAndLocation()
    }
    fun fetchPharmacies() {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
       return
        }
        fusedLocationProvider.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                val filter = "circle:$lon,$lat,5000" // 5km radius

                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.GEOAPIFY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(GeoapifyService::class.java)
                val call = service.getNearbyPharmacies(filter = filter)

                call.enqueue(object : Callback<GeoapifyResponse> {
                    override fun onResponse(call: Call<GeoapifyResponse>, response: Response<GeoapifyResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.features?.forEach {
                                Log.d("Pharmacy", "📍 ${it.properties.name ?: "Unknown"} - ${it.properties.street}, ${it.properties.city}")
                            }
                        } else {
                            Log.e("Geoapify", "Response error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<GeoapifyResponse>, t: Throwable) {
                        Log.e("Geoapify", "Network error: ${t.localizedMessage}")
                    }
                })
            } else {
                Log.e("Geoapify", "Location is null")
            }
        }
    }
//TODO ask for permission, Check if location is opened
fun checkPermissionsAndLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
    } else {
        checkLocationEnabled()
    }
}

    fun checkLocationEnabled() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // ✅ Permission + GPS ready — fetch data!
            fetchPharmacies()
        }

        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, 2001)
                } catch (ex: IntentSender.SendIntentException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    //TODO handle if changed location or permission status changed
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkLocationEnabled() // Permission OK, now check GPS
        } else {
            Toast.makeText(this, "Location permission is needed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            // ✅ GPS enabled after dialog
            fetchPharmacies()
        } else {
            Toast.makeText(this, "GPS must be enabled to show nearby pharmacies", Toast.LENGTH_SHORT).show()
        }
    }

}