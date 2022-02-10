package com.example.bustracker24x7.driver

import com.example.bustracker24x7.R
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapHelper {

    companion object{
        private const val ZOOM_LEVEL = 18
        private const val TILT_LEVEL = 25
    }

    fun buildCameraUpdate(latLng: LatLng): CameraUpdate{
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .tilt(TILT_LEVEL.toFloat())
                .zoom(ZOOM_LEVEL.toFloat())
                .build()
        return CameraUpdateFactory.newCameraPosition(cameraPosition)
    }

    fun getDriverMarkerOptions(position: LatLng): MarkerOptions{
        val options = getMarketOptions(R.drawable.green, position)
        options.flat(false)
        return options
    }

//    fun getDriverMakerOptions(position: LatLng): MarkerOptions{
//        val options = getDriverMarkerOptions(R.drawable.car_icon, position)
//        options.flat(false)
//        return options
//    }

    private fun getMarketOptions(resource: Int, position: LatLng): MarkerOptions {
        return MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(resource))
                .position(position)

    }

}