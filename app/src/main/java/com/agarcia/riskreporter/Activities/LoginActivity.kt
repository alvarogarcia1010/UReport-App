package com.agarcia.riskreporter.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.agarcia.riskreporter.Fragments.Register.RegisterFragment
import com.agarcia.riskreporter.R
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.content.Context


class LoginActivity : AppCompatActivity(), RegisterFragment.Internet {
    override fun internetTest(): Boolean = isNetworkAvailable()

    fun isNetworkAvailable() : Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }
}
