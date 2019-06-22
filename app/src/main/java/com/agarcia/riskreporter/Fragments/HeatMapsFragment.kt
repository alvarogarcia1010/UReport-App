package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat

import com.agarcia.riskreporter.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class HeatMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var heatMapFragment: SupportMapFragment
    private lateinit var heatMap: GoogleMap
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLong :LatLng


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_heat_maps, container, false)

        initMap()

        return view
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        setUpMap(googleMap)
    }

    private fun initMap()
    {
        heatMapFragment = childFragmentManager.findFragmentById(R.id.heat_map) as SupportMapFragment
        heatMapFragment.getMapAsync(this)

        fusedLocationCLient = LocationServices.getFusedLocationProviderClient(activity as Activity)

    }

    private fun setUpMap(googleMap: GoogleMap)
    {
        checkSelfPermission()

        heatMap = googleMap

        heatMap.uiSettings.isZoomControlsEnabled = true

        heatMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        fusedLocationCLient.lastLocation.addOnSuccessListener(activity as Activity)
        {
            if(it != null)
            {
                lastLocation = it
                currentLatLong = LatLng(it.latitude, it.longitude)

                heatMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }

    }

    private fun checkSelfPermission()
    {
        if(ActivityCompat.checkSelfPermission(activity as Activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
