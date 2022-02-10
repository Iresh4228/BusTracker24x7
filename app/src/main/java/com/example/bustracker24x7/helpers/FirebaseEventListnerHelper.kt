package com.example.bustracker24x7.helpers

import com.example.bustracker24x7.interfaces.FirebaseDriverListner
import com.example.bustracker24x7.model.Driver1
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


class FirebaseEventListnerHelper ( private val firebaseDriverListner: FirebaseDriverListner) : ChildEventListener {
    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val driver = p0.getValue(Driver1::class.java)
        firebaseDriverListner.onDriverAdded(driver!! as Driver1)
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        val driver = p0.getValue(Driver1::class.java)
        firebaseDriverListner.onDriverUpdated(driver!! as Driver1)
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        val driver = p0.getValue(Driver1::class.java)
        firebaseDriverListner.onDriverRemoved(driver!! as Driver1)
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onCancelled(p0: DatabaseError) {
    }


}


