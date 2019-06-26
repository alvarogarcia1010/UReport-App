package com.agarcia.riskreporter.Fragments.Register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Activities.MainActivity
import com.agarcia.riskreporter.Database.Models.User

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.util.*

class RegisterFragment : Fragment() {

    lateinit var picture: Button
    lateinit var gallery : Button
    lateinit var buttonN : Button

    lateinit var photo : String

    private var listenerTool : Internet? = null

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_CAPTURE_GALLERY= 0

    var selectedPhotoUri: Uri? = null

    private val PERMISSION_REQUEST_CODE: Int = 101
    private val PERMISSION_REQUEST_CODE1: Int = 102

    private lateinit var progress : ProgressBar

    interface Internet{

        fun internetTest() : Boolean
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picture = view.register_image_camera

        gallery = view.register_image_gallery

        buttonN = view.register_btn_next

        progress = view.progress_bar_register
        progress.visibility = View.GONE

        val companies = arrayOf("UCA")

        val adapter = ArrayAdapter<String> (view.context, android.R.layout.simple_dropdown_item_1line, companies)

        view.register_et_company.setAdapter(adapter)

        register_btn_next.setOnClickListener {
            if(validate()){
                val nextAction = RegisterFragmentDirections.step2Register(
                    photo,
                    register_et_fullname.text.toString(),
                    register_et_email.text.toString(),
                    register_et_company.text.toString()
                )
                Navigation.findNavController(it).navigate(nextAction)
            }
        }

        picture.setOnClickListener {
            if (checkPermission()){
                if(listenerTool!!.internetTest()){
                    takePicture()
                }else{
                    Snackbar.make(this.view!!, "Verifique su conexión!", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                requestPermission()
            }
        }

        gallery.setOnClickListener {
            if (checkPermissionGallery()){
                if(listenerTool!!.internetTest()){
                    selectPicture()
                } else{
                    Snackbar.make(this.view!!, "Verifique su conexión!", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                requestPermissionGallery()
            }
        }

    }

    private fun validate(): Boolean{
        var valid = true

        if(register_et_email.text.toString().isEmpty()){
            register_et_email.error = "Campo vacío"
            valid = false
        } else{
            register_et_email.error = null
        }

        if(register_et_fullname.text.toString().isEmpty()){
            register_et_fullname.error = "Campo vacío"
            valid = false
        } else{
            register_et_fullname.error = null
        }

        if(register_et_company.text.toString().isEmpty()){
            register_et_company.error = "Campo vacío"
            valid = false
        } else{
            register_et_company.error = null
        }

        if(!::photo.isInitialized){
            Snackbar.make(this.view!!, "Favor selecicone una foto", Snackbar.LENGTH_SHORT).show()
            valid = false
        }
        return valid
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
        //    register_photo.setImageBitmap(bitmap)
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
                    register_photo.setImageBitmap(bitmap)
                    progress.visibility = View.GONE
                    photo = it.toString()
                    buttonN.isEnabled = true
                }
            }
            .addOnFailureListener {
                Log.d("photo", "Fallo al subir la imagen al almacenamiento: ${it.message}")
                progress.visibility = View.GONE
                Snackbar.make(this.view!!, "Favor verifique su conexión!", Snackbar.LENGTH_SHORT).show()
            }.addOnProgressListener {
                progress.visibility = View.VISIBLE
                buttonN.isEnabled = false
            }
    }

    override fun onDetach() {
        super.onDetach()
        listenerTool = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Internet) {
            listenerTool = context
        } else {
            throw RuntimeException("Se necesita una implementación de  la interfaz")
        }
    }

}
