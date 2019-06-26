package com.agarcia.riskreporter.Fragments.NewReport


import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
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
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.fragment_location.view.*

class LocationFragment : Fragment() , OnMapReadyCallback,  GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener
{

    private lateinit var locationMapFragment: SupportMapFragment
    private lateinit var locationMap: GoogleMap
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLong : LatLng

    private lateinit var longitude : String
    private lateinit var latitude : String

    lateinit var title : String
    lateinit var risk : String
    lateinit var description : String
    lateinit var date : String
    lateinit var image: String

    private lateinit var location : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_location, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_3)

        initMap()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val safeArgs = LocationFragmentArgs.fromBundle(it)
            title = safeArgs.title
            risk = safeArgs.riskLevel
            description = safeArgs.remark
            date = safeArgs.date
            image = safeArgs.urlImage
        }

        location = view.fr_location_et_location

        //Ya te dejo dos variables listas con la latitud y la longitud del marker
        fr_location_bt_next.setOnClickListener {
            if(verify()){
                val nextAction = LocationFragmentDirections.nextAction(
                    title,
                    description,
                    date,
                    image,
                    risk,
                    fr_location_et_location.text.toString(),
                    latitude,
                    longitude
                )
                Navigation.findNavController(it).navigate(nextAction)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        setUpMap(googleMap)
    }


    override fun onMarkerClick(p0: Marker) = false

    override fun onMarkerDragStart(marker: Marker?) {
        if (marker != null) {
            longitude = marker.position.longitude.toString()
            latitude = marker.position.latitude.toString()
            Log.d("Marker", "Marker " + marker.id  + " Drag@" + longitude + ", lat:" + latitude)
        }
    }

    override fun onMarkerDrag(marker: Marker) {
        return
    }

    override fun onMarkerDragEnd(marker: Marker) {
        if (marker != null) {
            longitude = marker.position.longitude.toString()
            latitude = marker.position.latitude.toString()
            Log.d("Marker", "Marker " + marker.id  + " Drag@" + longitude + ", lat:" + latitude)

        }
    }


    private fun initMap()
    {
        locationMapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment
        locationMapFragment.getMapAsync(this)

        fusedLocationCLient = LocationServices.getFusedLocationProviderClient(activity as Activity)

    }


    private fun setUpMap(googleMap: GoogleMap)
    {
        checkSelfPermission()

        locationMap = googleMap

        locationMap.setOnMarkerClickListener(this)
        locationMap.setOnMarkerDragListener(this)

        locationMap.uiSettings.isZoomControlsEnabled = true

        locationMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        fusedLocationCLient.lastLocation.addOnSuccessListener(activity as Activity)
        {
            if(it != null)
            {
                lastLocation = it
                currentLatLong = LatLng(it.latitude, it.longitude)

                longitude = it.longitude.toString()
                latitude = it.latitude.toString()

                placeMarker(currentLatLong)

                locationMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }

    }
    
    private fun placeMarker(location: LatLng, title:String="Valor por defecto"){
        val markerOption = MarkerOptions().position(location).draggable(true).title(title)
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_primary_small))

        locationMap.addMarker(markerOption)
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

    private fun verify() : Boolean{
        var valid = true

        if(location.text.toString().isEmpty()){
            location.error = "Campo vacío"
            valid = false
        }else{
            location.error = null
        }
        return valid
    }

}
