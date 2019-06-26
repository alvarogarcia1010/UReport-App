package com.agarcia.riskreporter.Fragments.Register


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Activities.MainActivity
import com.agarcia.riskreporter.Database.Models.User

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register_fragment_part2.*
import kotlinx.android.synthetic.main.fragment_register_fragment_part2.view.*


class RegisterFragmentPart2 : Fragment() {

    private lateinit var auth : FirebaseAuth

    private lateinit var userViewModel : UserViewModel

    private lateinit var name : String
    private lateinit var email : String
    private lateinit var image_url : String
    private lateinit var company : String

    private lateinit var photo2 : ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_register_fragment_part2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        arguments?.let {
            val safeArgs = RegisterFragmentPart2Args.fromBundle(it)
            name = safeArgs.name
            email = safeArgs.mail
            image_url = safeArgs.photo
            company = safeArgs.company
        }

        photo2 = view.register_photo2

        Glide.with(view!!.context).load(image_url).into(photo2)

        register_btn_login.setOnClickListener {
            val backAction = RegisterFragmentDirections.backAction()
            Navigation.findNavController(it).navigate(backAction)
        }

        register_btn_register.setOnClickListener {
            registrar()
        }

        auth = FirebaseAuth.getInstance()


    }

    private fun validate(): Boolean{
        var valid = true
        if(register_et_password2.text.toString().isEmpty()){
            register_et_password2.error = "Campo vacío"
            valid = false
        } else{
            register_et_password2.error = null
        }

        if(register_et_passwordconfirm.text.toString().isEmpty()){
            register_et_passwordconfirm.error = "Campo vacío"
            valid = false
        } else{
            register_et_passwordconfirm.error = null
        }

        if(register_et_password2.text.toString() != register_et_passwordconfirm.text.toString()){
            Snackbar.make(this.view!!, "Contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }


    private fun registrar(){
        if(!validate()){
            failedRegister()
            return
        }

        auth.createUserWithEmailAndPassword(email, register_et_password2.text.toString())
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Register", "Usuario creado correctamente con uid: ${it.result?.user?.uid}")
                registerOnFirebaseDatabase()
            }.addOnFailureListener {
                Log.d("Main", "Error al crear usuario: ${it.message}")
                Snackbar.make(this.view!!, "Registro Fallido. Verifique su conexión a internet.", Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun registerOnFirebaseDatabase(){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val reference = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(email,image_url,name,company, false)

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

    private fun failedRegister(){
        register_btn_register.isEnabled = true
    }

}
