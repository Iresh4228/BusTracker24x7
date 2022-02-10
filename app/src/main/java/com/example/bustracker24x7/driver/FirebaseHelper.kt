package com.example.bustracker24x7.driver

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.bustracker24x7.model.Driver
import com.example.bustracker24x7.model.Driver1
import com.google.android.gms.maps.model.LatLng

class FirebaseHelper constructor(driverId: String){

    companion object{
        private const val ONLINE_DRIVERS = "online_drivers"
    }

    private val onlineDriverDatabaseReference: DatabaseReference = FirebaseDatabase
            .getInstance()
            .reference
            .child(ONLINE_DRIVERS)
            .child(driverId)

    init {
        onlineDriverDatabaseReference
                .onDisconnect()
                .removeValue()
    }

    fun updateDriver(driver: Driver){
        onlineDriverDatabaseReference
                .setValue(driver)
        Log.e("Driver Info", "Updated")
    }

    fun updateDriver1(driver1: Driver1){
        onlineDriverDatabaseReference
                .setValue(driver1)
        Log.e("Driver Info", "Updated")
    }

    fun editDriver(driver: Driver){
        onlineDriverDatabaseReference
                .setValue(driver)
    }

    fun editDriver1(driver1: Driver1){
        onlineDriverDatabaseReference
                .setValue(driver1)
    }

    fun deleteDriver(){
        onlineDriverDatabaseReference
                .removeValue()
    }
}