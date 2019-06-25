package com.agarcia.riskreporter.Fragments.Register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Activities.MainActivity
import com.agarcia.riskreporter.Database.Models.User

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var auth : FirebaseAuth

    private lateinit var userViewModel : UserViewModel

    lateinit var picture: Button
    lateinit var gallery : Button

    lateinit var photo : String

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_CAPTURE_GALLERY= 0

    var selectedPhotoUri: Uri? = null

    private val PERMISSION_REQUEST_CODE: Int = 101
    private val PERMISSION_REQUEST_CODE1: Int = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        picture = view.register_image_camera

        gallery = view.register_image_gallery

        register_btn_register.setOnClickListener {
            registrar()
        }

        register_btn_login.setOnClickListener {
            val backAction = RegisterFragmentDirections.backAction()
            Navigation.findNavController(it).navigate(backAction)
        }

        picture.setOnClickListener {
            if (checkPermission()) takePicture() else requestPermission()
        }

        gallery.setOnClickListener {
            if (checkPermissionGallery()) selectPicture() else requestPermissionGallery()
        }

    }

    private fun registrar(){
        if(!validate()){
            failedRegister()
            return
        }

        auth.createUserWithEmailAndPassword(register_et_email.text.toString(), register_et_password.text.toString())
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Register", "Usuario creado correctamente con uid: ${it.result?.user?.uid}")
                registerOnFirebaseDatabase()
            }.addOnFailureListener {
                Log.d("Main", "Error al crear usuario: ${it.message}")
                Toast.makeText(view?.context,"Error al registrarse. Verificar campos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerOnFirebaseDatabase(){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val reference = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(register_et_email.text.toString(),photo,register_et_fullname.text.toString(),register_et_company.text.toString(),false)

        userViewModel.insertUser(user)

        reference.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Guardando datos en la base de datos")

                val mIntent = Intent(activity, MainActivity::class.java)
                startActivity(mIntent)
                activity?.finish()
            }.addOnFailureListener{
                Log.d("Register", "Fallo al guardar (setear) valores en la base de datos: ${it.message}")
            }

    }

    private fun validate(): Boolean{
        var valid = true
        return valid
    }

    private fun failedRegister(){
        Toast.makeText(view?.context,"Registro fallido", Toast.LENGTH_SHORT).show()
        register_btn_register.isEnabled = true
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
            fr_image_image.setImageBitmap(data.extras.get("data") as Bitmap)
        }

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(view!!.context.contentResolver, selectedPhotoUri)
            register_photo.setImageBitmap(bitmap)
            uploadImageToFirebaseStorage()
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

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val fileName = UUID.randomUUID().toString()

        val storage = FirebaseStorage.getInstance().getReference("/images/$fileName")

        storage.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d("photo", "Foto subida con Exito: ${it.metadata?.path}")

                storage.downloadUrl.addOnSuccessListener {
                    Log.d("photo", "File location: $it")

                    photo = it.toString()
                }
            }
            .addOnFailureListener {
                Log.d("photo", "Fallo al subir la imagen al almacenamiento: ${it.message}")
            }
    }
}
