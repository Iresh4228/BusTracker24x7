package com.example.bustracker24x7

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import com.example.bustracker24x7.driver.FirebaseHelper
import com.example.bustracker24x7.driver.GoogleMapHelper
import com.example.bustracker24x7.driver.MarkerAnimationHelper
import com.example.bustracker24x7.driver.UiHelper
import com.example.bustracker24x7.interfaces.IPositiveNegativeListener
import com.example.bustracker24x7.interfaces.LatLngInterpolator
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.example.bustracker24x7.model.Driver
import com.example.bustracker24x7.model.Driver1

class MapActivity : AppCompatActivity() {

    companion object{
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationFlag = true
    private var driverOnlineFlag = false
    private var currentPositionMarke: Marker? = null
    private val googleMapHelper = GoogleMapHelper()
    private val firebaseHelper = FirebaseHelper("0000")
    private val firebaseHelper1 = FirebaseHelper("0001")
    private val markerAnimationHelper = MarkerAnimationHelper()
    private val uiHelper = UiHelper()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment
        mapFragment.getMapAsync(object : OnMapReadyCallback{
            override fun onMapReady(p0: GoogleMap?){
                googleMap = p0!!
            }
        })

        createLocationCallBack()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = uiHelper.getLocationRequest()
        if(!uiHelper.isPlayServicesAvailable(this)){
            Toast.makeText(this,"Play Services did not installed!", Toast.LENGTH_SHORT).show()
            finish()
        }else requestLocationUpdate()
        val driverStatusTextView = findViewById<TextView>(R.id.driverStatusTextView)
        findViewById<SwitchCompat>(R.id.driverStatusSwitch).setOnCheckedChangeListener { _, b->
            driverOnlineFlag = b
            if(driverOnlineFlag) driverStatusTextView.text = "Online"
            else{
                driverStatusTextView.text = "Offline"
                firebaseHelper.deleteDriver()
                firebaseHelper1.deleteDriver()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate(){
        if (!uiHelper.isHaveLocationPermission(this)){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        if (uiHelper.isLocationProviderEnabled(this))
            uiHelper.showPositiveDialogWithListener(this, "Enabale GPS Service", resources.getString(R.string.location_content), object : IPositiveNegativeListener {
                override fun onPositive() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }, "Turn On", false)
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun createLocationCallBack() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?){
                super.onLocationResult(locationResult)
                if (locationResult!!.lastLocation == null) return
                val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                Log.e("Location",latLng.latitude.toString() + " , " + latLng.longitude)
                if (locationFlag){
                    locationFlag = false
                    animateCamera(latLng)
                }
                if (driverOnlineFlag) firebaseHelper.updateDriver(Driver(lat = latLng.latitude, lng = latLng.longitude))
                showOrAnimateMarker(latLng);
                if (driverOnlineFlag) firebaseHelper1.updateDriver1(Driver1(lat = latLng.latitude, lng = latLng.longitude))
                showOrAnimateMarker(latLng);

            }
        }
    }

    private fun showOrAnimateMarker(latLng: LatLng) {
        if (currentPositionMarke == null)
            currentPositionMarke = googleMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng))
        else markerAnimationHelper.animateMarkerToGB(currentPositionMarke!!, latLng, LatLngInterpolator.Spherical())


    }

    private fun animateCamera(latLng: LatLng) {

        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            val value = grantResults[0]
            if (value == PERMISSION_DENIED){
                Toast.makeText(this,"Location Permission Denied", Toast.LENGTH_SHORT).show()
                finish()
            }else if (value == PERMISSION_GRANTED) requestLocationUpdate()
        }
    }
}