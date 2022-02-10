package com.example.bustracker24x7.interfaces

import com.example.bustracker24x7.model.Driver1

interface FirebaseDriverListner {

   fun onDriverAdded(driver: Driver1)

   fun onDriverRemoved(driver: Driver1)

   fun onDriverUpdated(driver: Driver1)

}