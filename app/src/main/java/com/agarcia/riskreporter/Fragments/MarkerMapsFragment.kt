package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.agarcia.riskreporter.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MarkerMapsFragment : Fragment(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener {

    private lateinit var markerMapFragment: SupportMapFragment
    private lateinit var markerMap: GoogleMap
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLong :LatLng

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_marker_maps, container, false)
        val btChange:View = view.findViewById(R.id.fr_google_maps_bt_change)

        initMap()

        btChange.setOnClickListener {
            Toast.makeText(activity, "Cambie de mapa", Toast.LENGTH_SHORT).show()
        }
        
        return view
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        setUpMap(googleMap)
    }


    override fun onMarkerClick(p0: Marker) = false

    private fun initMap()
    {
        markerMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        markerMapFragment.getMapAsync(this)

        fusedLocationCLient = LocationServices.getFusedLocationProviderClient(activity as Activity)

    }

    private fun setUpMap(googleMap: GoogleMap)
    {
        checkSelfPermission()

        markerMap = googleMap

        markerMap.setOnMarkerClickListener(this)
        markerMap.setOnMarkerDragListener(this)

        markerMap.uiSettings.isZoomControlsEnabled = true

        markerMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        val uca = LatLng(13.679959, -89.237012)
        placeMarker(uca, "Panal de abejas (A-42)")

        val uca2 = LatLng(13.679949, -89.235950)
        placeMarker(uca2, "Gotera (D-37)")



        fusedLocationCLient.lastLocation.addOnSuccessListener(activity as Activity)
        {
            if(it != null)
            {
                lastLocation = it
                currentLatLong = LatLng(it.latitude, it.longitude)
                placeMarker(currentLatLong)

                markerMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }

    }

    private fun placeMarker(location: LatLng, title:String="Valor por defecto"){
        val markerOption = MarkerOptions().position(location).draggable(true).title(title)
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_primary_small))

        markerMap.addMarker(markerOption)
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

    //Metodos para arrastrar marker
    override fun onMarkerDragStart(marker: Marker?) {
        if (marker != null) {
            Log.d("Marker", "Marker " + marker.id  + " Drag@" + marker.position)
        }
    }

    override fun onMarkerDrag(marker: Marker) {
        if (marker != null) {
            Log.d("Marker", "Marker " + marker.id  + " Drag@" + marker.position)
        }
    }

    override fun onMarkerDragEnd(marker: Marker) {
        if (marker != null) {
            Log.d("Marker", "Marker " + marker.id  + " Drag@" + marker.position)
        }
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }



}
