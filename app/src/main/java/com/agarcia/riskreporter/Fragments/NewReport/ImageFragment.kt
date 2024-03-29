package com.agarcia.riskreporter.Fragments.NewReport


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import java.util.*

class ImageFragment : Fragment() {

    lateinit var title : String
    lateinit var risk : String
    lateinit var description : String
    lateinit var date : String


    lateinit var picture: Button
    lateinit var gallery : Button

    lateinit var photo : String

    lateinit var buttonS : Button

    private lateinit var progress : ProgressBar

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_CAPTURE_GALLERY= 0

    var selectedPhotoUri: Uri? = null

    private val PERMISSION_REQUEST_CODE: Int = 101
    private val PERMISSION_REQUEST_CODE1: Int = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val safeArgs = ImageFragmentArgs.fromBundle(it)
            Log.d("hola", safeArgs.toString())
            title = safeArgs.title
            risk = safeArgs.riskLevel
            description = safeArgs.remark
            date = safeArgs.date
        }

        progress = view.findViewById(R.id.progressbar_image)
        progress.visibility = View.GONE

        picture = view.fr_image_camera

        gallery = view.fr_image_gallery

        picture.setOnClickListener {
           if (checkPermission()) takePicture() else requestPermission()
       }

        gallery.setOnClickListener {
           if (checkPermissionGallery()) selectPicture() else requestPermissionGallery()
       }

        buttonS = view.fr_image_bt_next

        buttonS.setOnClickListener {
            if (validate()) {
                val nextAction = ImageFragmentDirections.nextAction(
                    title,
                    description,
                    photo,
                    risk,
                    date
                )
                Navigation.findNavController(it).navigate(nextAction)
            }

        }

        fr_image_date.text = date


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
            selectedPhotoUri = data.data
            val bitmap = data.extras.get("data") as Bitmap
            //fr_image_image.setImageBitmap(data.extras.get("data") as Bitmap)
            uploadImageToFirebaseStorage(bitmap)
       }

       if(requestCode == 0 && resultCode == Activity.RESULT_OK && data!=null){
           selectedPhotoUri = data.data
           val bitmap = MediaStore.Images.Media.getBitmap(view!!.context.contentResolver, selectedPhotoUri)
           //fr_image_image.setImageBitmap(bitmap)
           uploadImageToFirebaseStorage(bitmap)
       }
   }

    private fun checkPermission():Boolean{
        return(ContextCompat.checkSelfPermission(view!!.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED )
    }

    private fun checkPermissionGallery():Boolean{
        return(ContextCompat.checkSelfPermission(view!!.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )
    }

    private fun requestPermission(){
        requestPermissions(arrayOf(Manifest.permission.CAMERA),PERMISSION_REQUEST_CODE)
    }

    private fun requestPermissionGallery(){
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_REQUEST_CODE1)
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap){
        if(selectedPhotoUri == null) return

        val fileName = UUID.randomUUID().toString()

        val storage = FirebaseStorage.getInstance().getReference("/images/$fileName")

        storage.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d("photo", "Foto subida con Exito: ${it.metadata?.path}")

                storage.downloadUrl.addOnSuccessListener {
                    Log.d("photo", "File location: $it")
                    fr_image_image.setImageBitmap(bitmap)
                    progress.visibility = View.GONE
                    photo = it.toString()
                    buttonS.isEnabled = true
                }
            }
            .addOnFailureListener {
                Log.d("photo", "Fallo al subir la imagen al almacenamiento: ${it.message}")
            }.addOnProgressListener {
                progress.visibility = View.VISIBLE
                buttonS.isEnabled = false

            }
    }

    private fun validate() : Boolean{
        var valid = true

        if(!::photo.isInitialized){
            Snackbar.make(this.view!!, "Favor seleccione una foto", Snackbar.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }

}
