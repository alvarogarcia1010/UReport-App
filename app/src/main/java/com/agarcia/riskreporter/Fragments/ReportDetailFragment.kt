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
import androidx.core.app.ActivityCompat

import com.agarcia.riskreporter.R
import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.fragment_report_detail.*

class ReportDetailFragment : Fragment() , OnMapReadyCallback,  GoogleMap.OnMarkerClickListener{

    private lateinit var markerMapFragment: SupportMapFragment
    private lateinit var markerMap: GoogleMap

    private lateinit var currentLatLong : LatLng
    private lateinit var title : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_report_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            val safeArgs = ReportDetailFragmentArgs.fromBundle(it)
            Log.d("report", safeArgs.report.toString())

            title = "${safeArgs.report.title} (${safeArgs.report.detailed_location})"
            currentLatLong = LatLng(safeArgs.report.latitude.toDouble(), safeArgs.report.longitude.toDouble())

            activity?.let { it1 -> Glide.with(it1).load(safeArgs.report.url_image).into(fr_detail_image) }

            toolbar_title.text = safeArgs.report.title
            fr_detail_risk_level.text = safeArgs.report.risk_level
            fr_detail_date.text = safeArgs.report.datetime
            fr_detail_remark.text = safeArgs.report.remark
        }

        initMap()

    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        setUpMap(googleMap)
    }


    override fun onMarkerClick(p0: Marker) = false

    private fun initMap()
    {
        markerMapFragment = childFragmentManager.findFragmentById(R.id.fr_detail_map) as SupportMapFragment
        markerMapFragment.getMapAsync(this)

    }

    private fun setUpMap(googleMap: GoogleMap)
    {
        checkSelfPermission()

        markerMap = googleMap

        markerMap.setOnMarkerClickListener(this)

        markerMap.uiSettings.isZoomControlsEnabled = true

        markerMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        placeMarker(currentLatLong, title)
        markerMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))

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

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }



}
