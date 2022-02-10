package com.example.bustracker24x7.interfaces

import com.google.android.gms.maps.model.LatLng

interface LatLngInterpolator1 {

    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

    class Spherical : LatLngInterpolator {

        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
            // http://en.wikipedia.org/wiki/Slerp
            val fromLat = Math.toRadians(a.latitude)
            val fromLng = Math.toRadians(a.longitude)
            val toLat = Math.toRadians(b.latitude)
            val toLng = Math.toRadians(b.longitude)
            val cosFromLat = Math.cos(fromLat)
            val cosToLat = Math.cos(toLat)

            // Computes Spherical interpolation coefficients.
            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle = Math.sin(angle)
            if (sinAngle < 1E-6) {
                return a
            }
            val temp1 = Math.sin((1 - fraction) * angle) / sinAngle
            val temp2 = Math.sin(fraction * angle) / sinAngle

            // Converts from polar to vector and interpolate.
            val x = temp1 * cosFromLat * Math.cos(fromLng) + temp2 * cosToLat * Math.cos(toLng)
            val y = temp1 * cosFromLat * Math.sin(fromLng) + temp2 * cosToLat * Math.sin(toLng)
            val z = temp1 * Math.sin(fromLat) + temp2 * Math.sin(toLat)

            // Converts interpolated vector back to polar.
            val lat = Math.atan2(z, Math.sqrt(x * x + y * y))
            val lng = Math.atan2(y, x)
            return LatLng(Math.toDegrees(lat), Math.toDegrees(lng))
        }

        private fun computeAngleBetween(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2), 2.0) + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(Math.sin(dLng / 2), 2.0)))
        }
    }
}