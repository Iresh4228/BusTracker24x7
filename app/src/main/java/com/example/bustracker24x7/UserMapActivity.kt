package com.example.bustracker24x7

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bustracker24x7.collection.MarkerCollection
import com.example.bustracker24x7.directionhelpers.FetchURL
import com.example.bustracker24x7.driver.GoogleMapHelper
import com.example.bustracker24x7.driver.UiHelper
import com.example.bustracker24x7.helpers.FirebaseEventListnerHelper
import com.example.bustracker24x7.helpers.MarkerAnimationHelper1
import com.example.bustracker24x7.interfaces.FirebaseDriverListner
import com.example.bustracker24x7.interfaces.IPositiveNegativeListener
import com.example.bustracker24x7.interfaces.LatLngInterpolator1
import com.example.bustracker24x7.model.Driver1
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_map.*


class UserMapActivity : AppCompatActivity(),  FirebaseDriverListner, OnMapReadyCallback, TaskLoadedCallback {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6161
        private const val ONLINE_DRIVERS = "online_drivers"
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var place1 : MarkerOptions
    private lateinit var place2 : MarkerOptions
    private lateinit var currentPolyline: Polyline
    private lateinit var getDirection : Button
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationFlag = true
    private lateinit var valueEventListener: FirebaseEventListnerHelper
    private val uiHelper = UiHelper()
    private lateinit var  mapFragment : MapFragment
    private val googleMapHelper = GoogleMapHelper()
    private val databaseReference = FirebaseDatabase.getInstance().reference.child(ONLINE_DRIVERS)

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_map)
//        getDirection = findViewById(R.layout.activity_user_map);
//        getDirection.setOnClickListener { FetchURL(this@UserMapActivity).execute(getUrl(place1.position, place2.position, "driving"), "driving") }

        //27.658143,85.3199503
        //27.667491,85.3208583
        place1 = MarkerOptions().position(LatLng(8.31591047880302, 80.40200604106718)).title("Location 1")
        place2 = MarkerOptions().position(LatLng(8.313178696690505, 80.40256899503488)).title("Location 2")
//        var mapFragment = fragmentManager
//                .findFragmentById(R.id.mapNearBy) as MapFragment
//        mapFragment.getMapAsync(this)


        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment
        mapFragment.getMapAsync{ googleMap = it}
        createLocationCallback()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = uiHelper.getLocationRequest()
        if (!uiHelper.isPlayServicesAvailable(this)){
            Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show()
            finish()
        }else requestLocationUpdate()
        valueEventListener = FirebaseEventListnerHelper(this)
        databaseReference.addChildEventListener(valueEventListener)
    }

    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String? {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key)
    }

    @SuppressLint("MissingPermissions")
    private fun requestLocationUpdate(){
        if (!uiHelper.isHaveLocationPermission(this)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        if (uiHelper.isLocationProviderEnabled(this))
            uiHelper.showPositiveDialogWithListener(this, resources.getString(R.string.need_location), resources.getString(R.string.location_content), object : IPositiveNegativeListener {
                override fun onPositive() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }, "Turn On", false)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult!!.lastLocation == null)return
                val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                Log.e("Location", latLng.latitude.toString() + " , " + latLng.longitude)
                if (locationFlag){
                    locationFlag = false
                    animateCamera(latLng)
                }
            }
        }
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            val value = grantResults[0]
            if (value == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show()
                finish()
            } else if (value == PackageManager.PERMISSION_GRANTED) requestLocationUpdate()
        }
    }

    override fun onDriverAdded(driver: Driver1) {
        val markerOptions = googleMapHelper.getDriverMarkerOptions(LatLng(driver.lat, driver.lng))
        val marker = googleMap.addMarker(markerOptions)
        marker.tag = driver.driverId
        MarkerCollection.insertMarker(marker)
        totalOnlineDrivers.text = resources.getString(R.string.total_online_drivers).plus("").plus(MarkerCollection.allMarkers().size)
    }

    fun onDriverAdd(driver1: Driver1) {
        val markerOptions = googleMapHelper.getDriverMarkerOptions(LatLng(driver1.lat, driver1.lng))
        val marker = googleMap.addMarker(markerOptions)
        marker.tag = driver1.driverId
        MarkerCollection.insertMarker(marker)
        totalOnlineDrivers.text = resources.getString(R.string.total_online_drivers).plus("").plus(MarkerCollection.allMarkers().size)
    }

    override fun onDriverRemoved(driver: Driver1) {
        MarkerCollection.removeMarker(driver.driverId)
        totalOnlineDrivers.text = resources.getString(R.string.total_online_drivers).plus("").plus(MarkerCollection.allMarkers().size)
    }

    override fun onDriverUpdated(driver: Driver1) {
        val marker = MarkerCollection.getMarker(driverId = driver.driverId)
        MarkerAnimationHelper1.animateMarkerToGB(marker!!, LatLng(driver.lat, driver.lng), LatLngInterpolator1.Spherical())

    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(valueEventListener)
        locationProviderClient.removeLocationUpdates(locationCallback)
        MarkerCollection.clearMarkers()
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = googleMap;
        Log.d("mylog", "Added Markers");
        googleMap.addMarker(place1);
        googleMap.addMarker(place2);

    }

    override fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline.remove()
        currentPolyline = googleMap.addPolyline(values[0] as PolylineOptions?);
    }


}