package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
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
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.util.TableInfo
import androidx.viewpager.widget.ViewPager
import com.agarcia.riskreporter.Adapters.ViewPagerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_google_maps.*


class GoogleMapsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_google_maps, container, false)

        //var viewPager : ViewPager = view.findViewById(R.id.viewpager_maps)
        //var tabs : TabLayout = view.findViewById(R.id.tabs_maps)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager!!)

        adapter.addFragment(MarkerMapsFragment(), "MarkerMap")
        adapter.addFragment(HeatMapsFragment(), "HeatMap")

        viewpager_maps.adapter = adapter

        tabs_maps.setupWithViewPager(viewpager_maps)
    }


}
