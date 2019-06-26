package com.agarcia.riskreporter.Fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.agarcia.riskreporter.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_reset_password.*
import android.content.Intent
import android.util.Log
import androidx.navigation.Navigation


class ResetPassword : Fragment() {

    private var auth : FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()



        bt_send_email_verification.setOnClickListener {

            if(rec_text.text.toString().isEmpty()){
                Toast.makeText(view.context, "Favor no dejar campo vacío", Toast.LENGTH_SHORT).show()
            } else{
                auth!!.sendPasswordResetEmail(rec_text.text.toString())
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful){
                            /*val mBuilder = AlertDialog.Builder(view.context)
                            mBuilder.setMessage("Favor Revisar Su Correo.")
                                .setCancelable(false)
                                .setPositiveButton("OK", {

                                })*/
                            Log.d("YES", "Correo de verificación con éxito.")
                            val builder = AlertDialog.Builder(view.context)
                            builder.setMessage("Favor Revisar Su Correo!")
                                .setCancelable(false)
                                .setPositiveButton("OK") { dialog, id ->
                                    val nextAction = ResetPasswordDirections.goToLogin()
                                    Navigation.findNavController(it).navigate(nextAction)
                                }
                            val b = builder.create()
                            b.show()
                        } else{
                            Toast.makeText(view.context, "Error al mandar correo", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }


}
