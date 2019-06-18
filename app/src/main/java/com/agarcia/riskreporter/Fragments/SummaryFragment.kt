package com.agarcia.riskreporter.Fragments


import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_summary.*
import kotlinx.android.synthetic.main.fragment_summary.view.*

class SummaryFragment : Fragment() {

    lateinit var picture: TextView
    lateinit var gallery : TextView


    val REQUEST_IMAGE_CAPTURE = 1

    val REQUEST_IMAGE_CAPTURE_GALLERY= 0

    var selectedPhotoUri: Uri? = null

    private val PERMISSION_REQUEST_CODE: Int = 101

    private val PERMISSION_REQUEST_CODE1: Int = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        val risks = arrayOf("Bajo", "Medio", "Alto")

        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line, risks)

        view.fr_summary_autocomplete.setAdapter(adapter)

        picture = view.fr_summary_addCamera

        gallery = view.fr_summary_addGallery

        picture.setOnClickListener {
            if (checkPermission()) takePicture() else requestPermission()
        }

        gallery.setOnClickListener {
            if (checkPermissionGallery()) selectPicture() else requestPermissionGallery()
        }

        view.fr_summary_next.setOnClickListener {
            val nextAction = SummaryFragmentDirections.nextAction("Descripcion", "Nivel de riesgo", "Titulo", "Imagen")
            Navigation.findNavController(it).navigate(nextAction)
        }


        view.fr_summary_autocomplete.threshold = 1

        view.fr_summary_autocomplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
            }

        view.fr_summary_autocomplete.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                view.fr_summary_autocomplete.showDropDown()
            }
        }

        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    takePicture()
                } else {
                    Toast.makeText(view!!.context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }

                return
            }

             PERMISSION_REQUEST_CODE1 -> {
                 if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                 ) {
                     selectPicture()
                 } else {
                     Toast.makeText(view!!.context, "Permission Denied", Toast.LENGTH_SHORT).show()
                 }
                 return
            }
        }

    }

    private fun takePicture(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun selectPicture(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data!=null){
            fr_summary_image.setImageBitmap(data.extras.get("data") as Bitmap)
        }

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(view!!.context.contentResolver, selectedPhotoUri)
            fr_summary_image.setImageBitmap(bitmap)
        }
    }

    private fun checkPermission():Boolean{
        return(ContextCompat.checkSelfPermission(view!!.context, CAMERA) == PackageManager.PERMISSION_GRANTED )
    }

    private fun checkPermissionGallery():Boolean{
        return(ContextCompat.checkSelfPermission(view!!.context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )
    }

    private fun requestPermission(){
        requestPermissions(arrayOf(CAMERA),PERMISSION_REQUEST_CODE)
    }

    private fun requestPermissionGallery(){
        requestPermissions(arrayOf(READ_EXTERNAL_STORAGE),PERMISSION_REQUEST_CODE1)
    }

}

