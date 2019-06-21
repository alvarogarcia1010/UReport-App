package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.agarcia.riskreporter.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class GoogleMapsFragment : Fragment() , OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_google_maps, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        fusedLocationCLient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

//        Add a marker in UCA and move the camera
        val uca = LatLng(13.679959, -89.237012)
        map.addMarker(MarkerOptions().position(uca).title("Panal de abejas (A-42)"))


        val uca2 = LatLng(13.679949, -89.235950)
        map.addMarker(MarkerOptions().position(uca2).title("Gotera (D-37)"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(uca))

        map.setOnMarkerClickListener(this)

        map.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap(){

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

        fusedLocationCLient.lastLocation.addOnSuccessListener(activity as Activity){
            if(it != null)
            {
                lastLocation = it
                val currentLatLong = LatLng(it.latitude, it.longitude)
                placeMarker(currentLatLong)

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 18f))
            }
        }
    }

    private fun placeMarker(location: LatLng){
        val markerOption = MarkerOptions().position(location)
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        map.addMarker(markerOption)
    }

}
