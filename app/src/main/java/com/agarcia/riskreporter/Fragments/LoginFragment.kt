package com.agarcia.riskreporter.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_login.*
import com.agarcia.riskreporter.Activities.MainActivity
import android.content.Intent
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {


    private lateinit var auth : FirebaseAuth

    private lateinit var progress : ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn_login.setOnClickListener {
            progress.visibility = View.VISIBLE
            login()
        }

        progress = view.findViewById(R.id.progressbar)
        progress.visibility = View.GONE

        login_btn_register.setOnClickListener {
            val nextAction = LoginFragmentDirections.nextAction()
            Log.d("holaaaaaaaa",it.toString())
            Navigation.findNavController(it).navigate(nextAction)

        }

        login_forgot_pass.setOnClickListener {
            val nextAction = LoginFragmentDirections.actionDestinationLoginToDestinationForgotPass()
            Navigation.findNavController(it).navigate(nextAction)
        }


        auth = FirebaseAuth.getInstance()

    }

    private fun login(){
        if(!validate()){
            failedLogin()
            return
        }

        auth.signInWithEmailAndPassword(login_et_email.text.toString(),login_et_password.text.toString())
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Login", "Loggeado correctamente: ${it.result?.user?.uid}")
                val mIntent = Intent(activity, MainActivity::class.java)
                mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mIntent)
                activity?.finish()
            }.addOnFailureListener {
                if(it.localizedMessage == "The password is invalid or the user does not have a password."){
                    progress.visibility = View.GONE
                    Toast.makeText(view?.context, "Contraseña Incorrecta", Toast.LENGTH_LONG).show()
                }
                else if(it.localizedMessage == "There is no user record corresponding to this identifier. The user may have been deleted."){
                    progress.visibility = View.GONE
                    Toast.makeText(view?.context, "No existe un usuario asociado a ese correo.", Toast.LENGTH_LONG).show()
                }else{
                    progress.visibility = View.GONE
                    Toast.makeText(view?.context, "Error al loggearse.", Toast.LENGTH_LONG).show()
                }

            }
    }

    private fun validate():Boolean{
        var valid = true

        if(login_et_email.text.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(login_et_email.text.toString()).matches()){
            login_et_email.error = "Ingrese un correo válido"
            valid = false
        } else{
            login_et_email.error = null
        }

        if(login_et_password.text.toString().isEmpty() || login_et_password.text.toString().length < 8){
            login_et_password.error = "Contraseña tiene que tener 8 cáracteres como minimo"
            valid = false
        } else{
            login_et_password.error = null
        }
        return valid
    }

    private fun failedLogin(){
            progress.visibility = View.GONE
            Log.d("Nose", progress.visibility.toString())
            Toast.makeText(view?.context,"Login fallido", Toast.LENGTH_SHORT).show()
            login_btn_login.isEnabled = true

    }


}
