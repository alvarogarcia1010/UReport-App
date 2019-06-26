package com.agarcia.riskreporter.Fragments


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
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Database.Models.User
import com.agarcia.riskreporter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_upate_perfil.*
import kotlinx.android.synthetic.main.fragment_upate_perfil.view.*
import java.util.*
import java.util.stream.Stream


class UpatePerfilFragment : Fragment() {

    lateinit var edittextName: EditText
    lateinit var user: FirebaseUser
    lateinit var currentuser: User
    lateinit var uid: String

    lateinit var picture: Button
    lateinit var gallery : Button

    lateinit var photo : String

    lateinit var buttonS : Button

    private lateinit var progress : ProgressBar

    var selectedPhotoUri: Uri? = null

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_CAPTURE_GALLERY= 0

    private val PERMISSION_REQUEST_CODE: Int = 101
    private val PERMISSION_REQUEST_CODE1: Int = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_upate_perfil, container, false)

        currentuser = User()
        user = FirebaseAuth.getInstance().currentUser!!

        user?.let {
            uid = user.uid
        }

        picture = view.update_image_camera
        gallery = view.update_image_gallery

        picture.setOnClickListener {
            if (checkPermission()) takePicture() else requestPermission()
        }

        gallery.setOnClickListener {
            if (checkPermissionGallery()) selectPicture() else requestPermissionGallery()
        }

        //buttonS = view.fr_image_bt_next

        view.update_save.setOnClickListener {
            val dbuser = FirebaseDatabase.getInstance().getReference("users").child(user.uid)

            val name = update_name.text.toString()
            val email = update_email.text.toString()
            val company = update_institution.text.toString()
            val url_image = photo

            val updateuser = User(email,url_image,name,company,false)

            dbuser.setValue(updateuser)
            Navigation.findNavController(it).navigate(R.id.go_back_to_profile)
        }



        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)

        super.onCreateOptionsMenu(menu, inflater)
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

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = data.extras.get("data") as Bitmap
            //fr_image_image.setImageBitmap(data.extras.get("data") as Bitmap)
            uploadImageToFirebaseStorage(bitmap)
        }

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
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
                    update_image.setImageBitmap(bitmap)
                    //progress.visibility = View.GONE
                    photo = it.toString()
                    //buttonS.isEnabled = true
                }
            }
            .addOnFailureListener {
                Log.d("photo", "Fallo al subir la imagen al almacenamiento: ${it.message}")
            }.addOnProgressListener {
                //progress.visibility = View.VISIBLE
                //buttonS.isEnabled = false

            }
    }








}
