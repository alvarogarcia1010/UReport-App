package com.agarcia.riskreporter.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Activities.MainActivity
import com.agarcia.riskreporter.Database.Models.User

import com.agarcia.riskreporter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        register_btn_register.setOnClickListener {
            registrar()
        }

        register_btn_login.setOnClickListener {
            val backAction = RegisterFragmentDirections.backAction()
            Navigation.findNavController(it).navigate(backAction)
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

        val user = User(register_et_email.text.toString(),"Something",register_et_fullname.text.toString(),register_et_company.text.toString(),false)

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
}
