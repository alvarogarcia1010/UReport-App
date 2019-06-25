package com.agarcia.riskreporter.Activities

import com.google.firebase.database.FirebaseDatabase


object Util {
    private var mDatabase: FirebaseDatabase? = null

    val database: FirebaseDatabase
        get() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance()
                mDatabase!!.setPersistenceEnabled(true)
            }
            return mDatabase as FirebaseDatabase
        }

}